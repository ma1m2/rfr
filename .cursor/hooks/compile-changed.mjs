#!/usr/bin/env node
import { spawnSync } from 'node:child_process'
import { relative } from 'node:path'
import { stdin } from 'node:process'

const input = JSON.parse(await new Response(stdin).text() || '{}')
const file = input.file_path || input.tool_input?.path || input.tool_input?.file_path || ''
const root = input.cwd || process.cwd()
const rel = relative(root, file).replaceAll('\\', '/')
const gw = process.platform === 'win32' ? '.\\gradlew.bat' : './gradlew'
let label
let cmd
if (rel.startsWith('rfr-auth/') && rel.endsWith('.java')) {
  [label, cmd] = ['rfr-auth', `${gw} -q :rfr-auth:compileJava`]
} else if (rel.startsWith('rfr-api/') && rel.endsWith('.java')) {
  [label, cmd] = ['rfr-api', `${gw} -q :rfr-api:compileJava`]
} else if (rel.startsWith('rfr-e2e/') && rel.endsWith('.java')) {
  [label, cmd] = ['rfr-e2e', `${gw} -q :rfr-e2e:compileTestJava`]
} else if (rel.startsWith('rfr-gql-client/src/') && /\.tsx?$/.test(rel)) {
  [label, cmd] = ['rfr-gql-client', 'npx --prefix rfr-gql-client tsc --noEmit -p rfr-gql-client']
} else {
  process.stdout.write('{}')
  process.exit(0)
}
const result = spawnSync(cmd, { cwd: root, encoding: 'utf8', shell: true, timeout: 170000, windowsHide: true })
const log = [result.stdout, result.stderr].filter(Boolean).join('\n').trim()
const msg = result.status === 0
  ? `Compilation of ${label} passed after editing ${rel}`
  : `Compilation of ${label} failed after editing ${rel}:\n${log}`
process.stdout.write(JSON.stringify({ additional_context: msg }))
