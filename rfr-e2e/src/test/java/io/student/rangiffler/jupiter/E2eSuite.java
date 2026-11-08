package io.student.rangiffler.jupiter;

import io.student.rangiffler.data.db.Databases;
import io.student.rangiffler.data.db.FlywaySchema;
import org.junit.platform.suite.api.BeforeSuite;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("io.student.rangiffler")
public final class E2eSuite {
  @BeforeSuite
  static void verifySchema() {
    System.out.println("E2eSuite.beforeSuite");
    new FlywaySchema(Databases.AUTH, "rangiffler-auth").verify();
    new FlywaySchema(Databases.API, "rangiffler-api").verify();
  }
}
