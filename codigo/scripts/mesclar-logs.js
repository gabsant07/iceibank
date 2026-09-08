import { existsSync, readFileSync } from "node:fs";
import { resolve } from "node:path";

const directory = resolve(process.argv[2] || "data");
const events = [];

for (let agencyId = 0; agencyId < 3; agencyId += 1) {
  const file = resolve(directory, `eventos-agencia-${agencyId}.jsonl`);
  if (!existsSync(file)) {
    continue;
  }
  const lines = readFileSync(file, "utf8").split(/\r?\n/).filter(Boolean);
  for (const line of lines) {
    events.push(JSON.parse(line));
  }
}

events
  .sort((left, right) =>
    left.lamportTimestamp - right.lamportTimestamp
    || left.agencyId - right.agencyId
    || String(left.wallClock).localeCompare(String(right.wallClock)))
  .forEach(event => {
    console.log(
      `Lamport ${event.lamportTimestamp} | Agency ${event.agencyId} | ${event.type} | ${event.details}`
    );
  });
