package com.daniel.test.example.lib.deployment;

import com.daniel.test.example.lib.runtime.FilterAuthHeader;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.AdditionalIndexedClassesBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ExampleTestLibProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleTestLibProcessor.class.getName());

    private static final String FEATURE = "example-test-lib";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerProvider(BuildProducer<AdditionalBeanBuildItem> additionalBeans,
                          BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
                          BuildProducer<AdditionalIndexedClassesBuildItem> additionalIndexedClassesBuildItem
    ) {
        LOGGER.info("START: Add filter custom header");
        additionalBeans.produce(AdditionalBeanBuildItem.unremovableOf(FilterAuthHeader.class));
        additionalIndexedClassesBuildItem
                .produce(new AdditionalIndexedClassesBuildItem(FilterAuthHeader.class.getName()));
        reflectiveClass.produce(
                ReflectiveClassBuildItem.builder(FilterAuthHeader.class).methods().fields().build());
        LOGGER.info("FINISH: Add filter custom header");
    }
}
