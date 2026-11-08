SELECT version
FROM flyway_schema_history
WHERE success = 1
  AND version IS NOT NULL
ORDER BY installed_rank
