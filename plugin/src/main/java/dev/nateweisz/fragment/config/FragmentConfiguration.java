package dev.nateweisz.fragment.config;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;

import javax.inject.Inject;
import java.io.File;

public abstract class FragmentConfiguration {

    @Inject
    public FragmentConfiguration(ObjectFactory objects, Project project) {
        getShouldGenerateShifts().convention(true);
        getOutputFile().convention(objects.fileProperty().fileValue(
                new File(project.getLayout().getBuildDirectory().get()
                        .getAsFile(), "pack.zip")
        ));
        getGeneratedDirectory().convention(objects.fileProperty().fileValue(
                new File(project.getLayout().getBuildDirectory().get().getAsFile(), "generated/")
        ));
    }
    /**
     * The file that contains all the necessary configuration.
     * It is recommended to break up your pack configuration into multiple files and this
     * can be achieved using the 'include "example.conf"' hocon syntax.
     */
    abstract public RegularFileProperty getPackFile();

    abstract public RegularFileProperty getOutputFile();

    abstract public RegularFileProperty getGeneratedDirectory();

    /**
     * Controls whether shifts should be generated.
     */
    abstract public Property<Boolean> getShouldGenerateShifts();
}