package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.ApplicationName.applicationName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageInfo.newImageInfo;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageQuery.newQuery;
import static io.leitstand.inventory.service.ImageReference.newImageReference;
import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.RoleImage.newRoleImage;
import static io.leitstand.inventory.service.RoleImages.newRoleImages;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsExactly;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class ImageValueObjectsTest {

    private static final ImageId             IMAGE_ID           = randomImageId();
    private static final ImageName           IMAGE_NAME         = imageName("image");
    private static final Version             IMAGE_VERSION      = new Version(1,0,0);
    private static final ImageType           IMAGE_TYPE         = imageType("type");
    private static final PlatformChipsetName PLATFORM_CHIPSET   = platformChipsetName("chipset");
    private static final ElementRoleName     ELEMENT_ROLE       = elementRoleName("role");
    private static final ApplicationName     APPLICATION_NAME   = applicationName("unittest");
    private static final Date                BUILD_DATE         = new Date();
    private static final String              BUILD_ID           = randomUUID().toString();
    private static final String              CATEGORY           = "category";
    private static final String              DESCRIPTION        = "description";
    private static final String              IMAGE_EXTENSION    = "ext";
    private static final String              ORGANIZATION       = "organization";
    
    @Test
    public void create_role_image() {
        RoleImage image = newRoleImage()
                          .withImageId(IMAGE_ID)
                          .withImageName(IMAGE_NAME)
                          .withImageType(IMAGE_TYPE)
                          .withImageVersion(IMAGE_VERSION)
                          .withPlatformChipset(PLATFORM_CHIPSET)
                          .build();
        
        assertThat(image.getImageId(),is(IMAGE_ID));
        assertThat(image.getImageName(),is(IMAGE_NAME));
        assertThat(image.getImageType(),is(IMAGE_TYPE));
        assertThat(image.getImageVersion(),is(IMAGE_VERSION));
        assertThat(image.getPlatformChipset(),is(PLATFORM_CHIPSET));
    }
    
    @Test
    public void create_role_images() {
        RoleImage image = mock(RoleImage.class);
        RoleImages images = newRoleImages()
                            .withElementRole(ELEMENT_ROLE)
                            .withImages(asList(image))
                            .build();
        
        assertThat(images.getElementRole(),is(ELEMENT_ROLE));
        assertThat(images.getImages(),containsExactly(image));
    }
    
    @Test
    public void create_image_info() {
        PlatformSettings platform = mock(PlatformSettings.class);
        PackageVersionInfo pkg = mock(PackageVersionInfo.class);
        Map<String,String> checksums = new TreeMap<>();
        checksums.put("SHA256", "dummy-sha256-checksum");
        
        ImageInfo imageInfo = newImageInfo()
                              .withApplications(APPLICATION_NAME)
                              .withBuildDate(BUILD_DATE)
                              .withBuildId(BUILD_ID)
                              .withCategory(CATEGORY)
                              .withChecksums(checksums)
                              .withDescription(DESCRIPTION)
                              .withElementRoles(ELEMENT_ROLE)
                              .withExtension(IMAGE_EXTENSION)
                              .withImageId(IMAGE_ID)
                              .withImageName(IMAGE_NAME)
                              .withImageState(CANDIDATE)
                              .withImageType(IMAGE_TYPE)
                              .withImageVersion(IMAGE_VERSION)
                              .withOrganization(ORGANIZATION)
                              .withPackages(pkg)
                              .withPlatformChipset(PLATFORM_CHIPSET)
                              .withPackages(asList(pkg))
                              .build();
                        
        assertThat(imageInfo.getApplications(),containsExactly(APPLICATION_NAME));
        assertThat(imageInfo.getBuildDate(),is(BUILD_DATE));
        assertThat(imageInfo.getBuildId(),is(BUILD_ID));
        assertThat(imageInfo.getCategory(),is(CATEGORY));
        assertThat(imageInfo.getChecksums(),is(checksums));
        assertThat(imageInfo.getDescription(),is(DESCRIPTION));
        assertNull(imageInfo.getElementName());
        assertThat(imageInfo.getElementRoles(),containsExactly(ELEMENT_ROLE));
        assertThat(imageInfo.getExtension(),is(IMAGE_EXTENSION));
        assertThat(imageInfo.getImageId(),is(IMAGE_ID));
        assertThat(imageInfo.getImageName(),is(IMAGE_NAME));
        assertThat(imageInfo.getImageState(),is(CANDIDATE));
        assertThat(imageInfo.getImageType(),is(IMAGE_TYPE));
        assertThat(imageInfo.getImageVersion(),is(IMAGE_VERSION));
        assertThat(imageInfo.getOrganization(),is(ORGANIZATION));
        assertThat(imageInfo.getPackages(),containsExactly(pkg));
        assertThat(imageInfo.getPlatformChipset(),is(PLATFORM_CHIPSET));
        assertThat(imageInfo.getPlatforms(),containsExactly(platform));
    }
    
    @Test
    public void create_image_reference() {
        ImageReference imageRef = newImageReference()
                                  .withBuildDate(BUILD_DATE)
                                  .withElementRoles(asList(ELEMENT_ROLE))
                                  .withImageId(IMAGE_ID)
                                  .withImageName(IMAGE_NAME)
                                  .withImageState(CANDIDATE)
                                  .withImageType(IMAGE_TYPE)
                                  .withImageVersion(IMAGE_VERSION)
                                  .withPlatformChipset(PLATFORM_CHIPSET)    
                                  .build();
                        
        assertThat(imageRef.getBuildDate(),is(BUILD_DATE));
        assertNull(imageRef.getElementName());
        assertThat(imageRef.getElementRoles(),containsExactly(ELEMENT_ROLE));
        assertThat(imageRef.getImageId(),is(IMAGE_ID));
        assertThat(imageRef.getImageName(),is(IMAGE_NAME));
        assertThat(imageRef.getImageState(),is(CANDIDATE));
        assertThat(imageRef.getImageType(),is(IMAGE_TYPE));
        assertThat(imageRef.getImageVersion(),is(IMAGE_VERSION));
        assertThat(imageRef.getPlatformChipset(),is(PLATFORM_CHIPSET));
    }
    
    @Test
    public void create_image_query() {
        ImageQuery query = newQuery()
                           .imageState(CANDIDATE)
                           .imageType(IMAGE_TYPE)
                           .imageVersion(IMAGE_VERSION)
                           .roleName(ELEMENT_ROLE)
                           .platformChipset(PLATFORM_CHIPSET)
                           .filter("filter")
                           .limit(100);
        
        assertThat(query.getElementRole(),is(ELEMENT_ROLE));
        assertThat(query.getFilter(),is("filter"));
        assertThat(query.getImageState(),is(CANDIDATE));
        assertThat(query.getImageType(),is(IMAGE_TYPE));
        assertThat(query.getImageVersion(),is(IMAGE_VERSION));
        assertThat(query.getPlatformChipset(),is(PLATFORM_CHIPSET));
        assertThat(query.getLimit(),is(100));
        
        
    }
    
}