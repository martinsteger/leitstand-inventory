package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.PackageInfo.newPackageInfo;
import static io.leitstand.inventory.service.PackageVersionInfo.newPackageVersionInfo;
import static io.leitstand.inventory.service.PackageVersionRef.newPackageVersionRef;
import static io.leitstand.inventory.service.Version.version;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsExactly;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class PackageValueObjectsTest {
    
    private static final Version PACKAGE_VERSION = version("1.0.0");
    private static final String ORGANIZATION = "organization";
    private static final String PACKAGE_NAME = "package";
    private static final String BUILD_ID = randomUUID().toString();
    private static final Date BUILD_DATE = new Date();
    private static final String PACKAGE_EXT = "ext";

    
    @Test
    public void create_package_version_ref() {
        PackageVersionRef ref = newPackageVersionRef()
                                .withOrganization(ORGANIZATION)
                                .withPackageName(PACKAGE_NAME)
                                .withPackageVersion(PACKAGE_VERSION)
                                .build();
        
        assertThat(ref.getOrganization(), is(ORGANIZATION));
        assertThat(ref.getPackageName(),is(PACKAGE_NAME));
        assertThat(ref.getPackageVersion(),is(PACKAGE_VERSION));
        
    }
    
    @Test
    public void create_package_version_info() {
        Map<String,String> checksums = new TreeMap<>();
        checksums.put("SHA256", "dummy-sha256-checksum");
        PackageVersionInfo info = newPackageVersionInfo()
                                  .withBuildDate(BUILD_DATE)
                                  .withBuildId(BUILD_ID)
                                  .withChecksums(checksums)
                                  .withOrganization(ORGANIZATION)
                                  .withPackageExtension(PACKAGE_EXT)
                                  .withPackageName(PACKAGE_NAME)
                                  .withPackageVersion(PACKAGE_VERSION)
                                  .build();
        
        assertThat(info.getBuildDate(),is(BUILD_DATE));
        assertThat(info.getBuildId(),is(BUILD_ID));
        assertThat(info.getChecksums(),is(checksums));
        assertThat(info.getOrganization(),is(ORGANIZATION));
        assertThat(info.getPackageExtension(),is(PACKAGE_EXT));
        assertThat(info.getPackageName(),is(PACKAGE_NAME));
        assertThat(info.getPackageVersion(),is(PACKAGE_VERSION));
    
    }
    
    @Test
    public void create_package_info() {
        PackageVersionRef ref = mock(PackageVersionRef.class);
        
        PackageInfo pkg = newPackageInfo()
                          .withName(PACKAGE_NAME)
                          .withOrganization(ORGANIZATION)
                          .withVersions(asList(ref))
                          .build();
        
        assertThat(pkg.getName(),is(PACKAGE_NAME));
        assertThat(pkg.getOrganization(),is(ORGANIZATION));
        assertThat(pkg.getVersions(),containsExactly(ref));
                          
    }
    
}
