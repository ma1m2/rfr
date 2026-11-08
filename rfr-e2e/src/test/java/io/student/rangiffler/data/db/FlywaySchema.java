package io.student.rangiffler.data.db;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class FlywaySchema implements Schema {
  private final Databases database;
  private final String schema;

  public FlywaySchema(Databases database, String schema) {
    this.database = database;
    this.schema = schema;
  }

  @Override
  public void verify() {
    Set<String> scripts = new TreeSet<>(this.scripts());
    if (scripts.isEmpty()) {
      throw new IllegalStateException(
          "Backend Flyway scripts for " + this.schema
              + " were not found on the classpath; run e2e through Gradle so migrations are copied from the backend"
      );
    }
    Set<String> history = new TreeSet<>(this.history());
    history.remove("0");
    if (history.equals(scripts)) {
      return;
    }
    Set<String> pending = new TreeSet<>(scripts);
    pending.removeAll(history);
    Set<String> extra = new TreeSet<>(history);
    extra.removeAll(scripts);
    throw new IllegalStateException(
        "Flyway versions in local " + this.schema + " " + history
            + " do not match backend migrations " + scripts
            + "; restart the backend for " + this.schema
            + " so Flyway applies " + pending
            + " or recreate MySQL volume mysql-volume if this branch is older and extra versions are " + extra
    );
  }

  private List<String> history() {
    try {
      return Jdbc.client(this.database)
          .sql(Jdbc.sql("sql/flyway/select-versions.sql"))
          .query((rs, rowNum) -> rs.getString("version"))
          .list();
    } catch (BadSqlGrammarException exception) {
      throw new IllegalStateException(
          "Flyway history table is missing in " + this.schema + "; start the backend for " + this.schema + " so Flyway migrates",
          exception
      );
    } catch (DataAccessException exception) {
      throw new IllegalStateException(
          "Cannot read Flyway history in " + this.schema + "; start MySQL on port 3306",
          exception
      );
    }
  }

  private List<String> scripts() {
    Resource[] resources;
    try {
      resources = new PathMatchingResourcePatternResolver()
          .getResources("classpath:db/migration/" + this.schema + "/V*__*.sql");
    } catch (IOException exception) {
      throw new UncheckedIOException("Cannot list Flyway scripts for " + this.schema, exception);
    }
    List<String> versions = new ArrayList<>();
    for (Resource resource : resources) {
      String name = resource.getFilename();
      if (name == null) {
        continue;
      }
      int separator = name.indexOf("__");
      if (!name.startsWith("V") || separator < 2) {
        continue;
      }
      versions.add(name.substring(1, separator));
    }
    return versions;
  }
}
