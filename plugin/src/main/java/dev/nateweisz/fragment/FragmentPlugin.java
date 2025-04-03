package dev.nateweisz.fragment;

import dev.nateweisz.fragment.config.FragmentConfiguration;
import dev.nateweisz.fragment.tasks.GeneratePackTask;
import org.gradle.api.*;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.TaskProvider;

public class FragmentPlugin implements Plugin<Project> {
    public void apply(Project project) {
        // fragment config
        FragmentConfiguration config = project.getExtensions().create(
                "fragment",
                FragmentConfiguration.class);

        TaskProvider<GeneratePackTask> genPackTask = project.getTasks().register("generatePack", GeneratePackTask.class, task -> {
            task.setGroup("fragment");
            task.setDescription("Generates mappings and a minecraft compatible ");
            task.dependsOn("processResources");
        });

        genPackTask.get().getPackFile().set(config.getPackFile());
        genPackTask.get().getOutputFile().set(config.getOutputFile());
        genPackTask.get().getGeneratedDirectory().set(config.getGeneratedDirectory());

        project.getTasks().register("publishPack"); // TODO: Mainly for CI/CD
    }
}
