/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementImageReference.newElementImageReference;
import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static io.leitstand.inventory.service.ElementImageState.CACHED;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.ReasonCode.IVT0341E_ELEMENT_IMAGE_ACTIVE;
import static io.leitstand.inventory.service.Version.version;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.Flow;
import io.leitstand.commons.tx.SubtransactionService;
import io.leitstand.inventory.service.ElementImageReference;
import io.leitstand.inventory.service.ImageId;

@RunWith(MockitoJUnitRunner.class)
public class ElementImageManagerTest {
	
	private static final ElementImageReference ACTIVE_IMAGE_REF =  newElementImageReference()
																   .withImageType(imageType("lxd"))
																   .withImageName(imageName("JUNIT"))
																   .withImageVersion(version("1.0.0"))
																   .withElementImageState(ACTIVE)
																   .build();

	private static final ElementImageReference CACHED_IMAGE_REF =  newElementImageReference()
			   																.withImageType(imageType("lxd"))
			   																.withImageName(imageName("JUNIT"))
			   																.withImageVersion(version("1.0.0"))
			   																.withElementImageState(CACHED)
			   																.build();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;

	@Mock
	private Messages messages;

	@Mock
	private SubtransactionService service;
	
	@InjectMocks
	private ElementImageManager manager = new ElementImageManager();

	@Test
	public void attempt_to_remove_an_active_installed_image_raises_conflict_exception() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0341E_ELEMENT_IMAGE_ACTIVE));
		
		Element_Image image = mock(Element_Image.class);
		doReturn(Boolean.TRUE).when(image).isActive();
		when(repository.execute(any(Query.class))).thenReturn(image);

		manager.removeElementImage(mock(Element.class),
								   mock(ImageId.class));
	}
	
	@Test
	public void remove_inactive_installed_image() {
		Element_Image image = mock(Element_Image.class);
		when(repository.execute(any(Query.class))).thenReturn(image);
		
		manager.removeElementImage(mock(Element.class), 
								   mock(ImageId.class));
		
		verify(repository).remove(image);
	}
	
	@Test
	public void do_nothing_when_element_image_to_be_removed_does_not_exist() {
		manager.removeElementImage(mock(Element.class), 
								   mock(ImageId.class));
		
		verify(repository,never()).remove(any());
	}
	
	@Test
	public void mark_existing_cached_image_as_active() {
		Element_Image cached = mock(Element_Image.class);
		when(cached.getImageType()).thenReturn(imageType("lxd"));
		when(cached.getImageName()).thenReturn(imageName("JUNIT"));
		when(cached.getImageVersion()).thenReturn(version("1.0.0"));
		when(cached.getElementImageState()).thenReturn(CACHED);
		when(repository.execute(any(Query.class))).thenReturn(asList(cached));

		manager.storeElementImages(mock(Element.class),
								   asList(ACTIVE_IMAGE_REF));
		

		verify(cached).setElementImageState(ACTIVE);
		verify(repository,never()).add(cached);
		
	}
	
	@Test
	public void add_new_installed_image_if_not_associated_with_element() {
		Element_Image newImage = mock(Element_Image.class);
		when(newImage.getImageType())
		.thenReturn(imageType("lxd"));
		
		when(newImage.getImageVersion())
		.thenReturn(version("1.0.0"));
		
		Image image = mock(Image.class);
		when(image.getImageType()).thenReturn(imageType("lxd"));
		when(image.getImageVersion()).thenReturn(version("1.0.0"));
		
		when(repository.execute(any(Query.class)))
		.thenReturn(emptyList())
		.thenReturn(image);
		
		ArgumentCaptor<Element_Image> imageCaptor = ArgumentCaptor.forClass(Element_Image.class);
		doNothing().when(repository).add(imageCaptor.capture());
		
		manager.storeElementImages(mock(Element.class),
										asList(ACTIVE_IMAGE_REF));
		

		assertEquals(version("1.0.0"),imageCaptor.getValue().getImageVersion());
		assertEquals(imageType("lxd"),imageCaptor.getValue().getImageType());
		assertTrue(imageCaptor.getValue().isActive());
	}
	
	@Test
	public void attempt_to_register_an_unknown_image_fails_silently() {
		Image image = mock(Image.class);
		when(service.run(any(Flow.class))).thenReturn(image);

		when(repository.execute(any(Query.class)))
		.thenReturn(emptyList())
		.thenReturn(null);

		
		manager.storeElementImages(mock(Element.class,withSettings().defaultAnswer(RETURNS_MOCKS)),
								   asList(ACTIVE_IMAGE_REF));
		
		verify(service).run(any(Flow.class));
		verify(repository).add(any(Element_Image.class));
	}
	
	@Test
	public void remove_existing_references() {
		Element_Image cached = mock(Element_Image.class);
		when(cached.getImageType()).thenReturn(imageType("lxd"));
		when(cached.getImageName()).thenReturn(imageName("JUNIT"));
		when(cached.getImageVersion()).thenReturn(version("1.0.0"));
		when(cached.getElementImageState()).thenReturn(CACHED);
		when(repository.execute(any(Query.class))).thenReturn(asList(cached));

		manager.storeElementImages(mock(Element.class),
								   emptyList());
		

		verify(repository).remove(cached);
	}	
	
}
