package net.lr.tasklist.ui;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Tasklist UI")
@interface TaskUIConfig {
    String title() default "Tasklist";
    int numTasks() default 20;
}
