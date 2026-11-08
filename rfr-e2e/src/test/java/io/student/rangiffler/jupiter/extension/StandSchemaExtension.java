package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.data.db.Databases;
import io.student.rangiffler.data.db.FlywaySchema;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

public final class StandSchemaExtension implements LauncherSessionListener {
  @Override
  public void launcherSessionOpened(LauncherSession session) {
    System.out.println(
        "StandSchemaExtension.launcherSessionOpened worker="
            + System.getProperty("org.gradle.test.worker")
            + " pid="
            + ProcessHandle.current().pid()
    );
    new FlywaySchema(Databases.AUTH, "rangiffler-auth").verify();
    new FlywaySchema(Databases.API, "rangiffler-api").verify();
  }
}
