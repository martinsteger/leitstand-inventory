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

import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Platform.findByPlatformId;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageInfo.newImageInfo;
import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.ImageState.RELEASE;
import static io.leitstand.inventory.service.ImageState.REVOKED;
import static io.leitstand.inventory.service.ImageState.SUPERSEDED;
import static io.leitstand.inventory.service.ImageType.LXC;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Date;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.Version;

public class ManageRoleBasedImagesIT extends InventoryIT{
	
	private static final PlatformId PLATFORM_ID = randomPlatformId();
	private static final PlatformName PLATFORM_NAME = platformName(ManageRoleBasedImagesIT.class.getName());
	
	private DefaultImageService service;
	private Repository repository;
	private ImageInfo ref;
	
	@Before
	public void initTestEnvironment() {
		repository = new Repository(getEntityManager());

		PlatformProvider platforms = new PlatformProvider(repository);
		
		service = new DefaultImageService(mock(PackageVersionService.class), 
										  platforms,
										  repository,
										  mock(DatabaseService.class),
										  mock(Messages.class),
										  mock(Event.class));
		
		transaction(()->{

			Platform platform = repository.addIfAbsent(findByPlatformId(PLATFORM_ID),
													   () -> new Platform(PLATFORM_ID,PLATFORM_NAME));
			
			repository.addIfAbsent(findRoleByName(new ElementRoleName("unit-element_type")), 
								  ()-> new ElementRole(new ElementRoleName("unit-element_type"), DATA));
			
			ImageInfo image = newImageInfo()
							  .withImageId(randomImageId())
							  .withBuildDate(new Date())
							  .withElementRole(new ElementRoleName("unit-element_type"))
							  .withPlatformId(PLATFORM_ID)
							  .withPlatformName(PLATFORM_NAME)
							  .withOrganization("net.rtbrick")
							  .withImageType(LXC)
							  .withImageName(ImageName.valueOf("JUNIT"))
							  .withImageVersion(new Version(1,0,0))
							  .build();
										  
			
			service.storeImage(image);
				service.updateImageState(image.getImageId(), RELEASE);
				ref = image;
			});
		
	}
	
	@Test
	public void newly_added_image_is_candidate_image() {
		ImageInfo image = newImageInfo()
				  		 .withImageId(randomImageId())
				  		 .withImageState(CANDIDATE)
				  		 .withImageName(ImageName.valueOf("JUNIT"))
				  		 .withBuildDate(new Date())
				  		 .withElementRole(new ElementRoleName("unit-element_type"))
				  		 .withPlatformId(PLATFORM_ID)
				  		 .withPlatformName(PLATFORM_NAME)
				  		 .withOrganization("net.rtbrick")
				  		 .withImageType(LXC)
				  		 .withImageVersion(new Version(1,0,1))
				  		 .build();

		transaction(()->{
			service.storeImage(image);
		});
		transaction(()->{
			ImageInfo stored = service.getImage(image.getImageId());
			assertEquals(CANDIDATE,stored.getImageState());
		});
		
	}
	
	
	@Test
	public void make_candidate_to_default_and_supersed_existing_default() {
		ImageInfo image = newImageInfo()
		  		 		  .withImageId(randomImageId())
		  		 		  .withImageType(LXC)
		  		 		  .withImageName(ImageName.valueOf("JUNIT"))
		  		 		  .withImageState(CANDIDATE)
		  		 		  .withImageVersion(new Version(2,0,0))
		  		 		  .withBuildDate(new Date())
		  		 		  .withElementRole(new ElementRoleName("unit-element_type"))
		  		 		  .withPlatformId(PLATFORM_ID)
		  		 		  .withPlatformName(PLATFORM_NAME)
		  		 		  .withOrganization("net.rtbrick")
		  		 		  .build();
		transaction(()->{
			service.storeImage(image);
		});
		transaction(()->{
			service.getImage(image.getImageId());
		});
		
		transaction(()->{
			ImageInfo stored = service.getImage(image.getImageId());
			assertEquals(CANDIDATE,stored.getImageState());
			service.updateImageState(stored.getImageId(), RELEASE);
		});
		
		transaction(()->{
			ImageInfo stored = service.getImage(image.getImageId());
			assertEquals(RELEASE,stored.getImageState());
			ref = service.getImage(ref.getImageId());
			assertEquals(SUPERSEDED,ref.getImageState());
		});
		
	}
	
	@Test
	public void revoke_candidate_image_has_no_effect_on_default_image() {
		ImageInfo image = newImageInfo()
		  		 		  .withImageId(randomImageId())
		  		 		  .withBuildDate(new Date())
		  		 		  .withImageType(LXC)
		  		 		  .withImageName(ImageName.valueOf("JUNIT"))
		  		 		  .withImageVersion(new Version(1,0,1))
		  		 		  .withElementRole(new ElementRoleName("unit-element_type"))
		  		 		  .withPlatformId(PLATFORM_ID)
		  		 		  .withPlatformName(PLATFORM_NAME)
		  		 		  .withOrganization("net.rtbrick")
		  		 		  .build();
		transaction(()->{
			service.storeImage(image);
		});
		transaction(()->{
			service.updateImageState(image.getImageId(),REVOKED);
		});
		transaction(()->{
			ImageInfo stored = service.getImage(image.getImageId());
			assertEquals(ImageState.REVOKED,stored.getImageState());
			ref = service.getImage(ref.getImageId());
			assertEquals(ImageState.RELEASE,ref.getImageState());
		});
	}
	
	
	
	
}
