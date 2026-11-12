# PvP Studio (Fabric, Minecraft 1.21.11)

Client-side, cosmetic/informational texture and PvP settings menu. No automation, no gameplay advantage features.

## Build
Requirements: JDK 21.

```
gradle wrapper --gradle-version 9.2.1     # once, if no wrapper is present (needs a system Gradle)
./gradlew build                           # jar in build/libs/
./gradlew runClient                       # dev client
```
Versions are pinned in `gradle.properties`. If one is not found, check https://fabricmc.net/develop and edit it.

## Use
Press **Right Shift** (rebindable under Controls > PvP Studio) to open the menu.
Data lives in `.minecraft/config/pvpstudio/` (`config.json`, `packs/`, `profiles/`, `backups/`).

## Roadmap
1. Skeleton (this stage): entrypoint, keybind, JSON config with migration, menu shell, safe-path utility
2. Texture engine: slots, pack import (zip/folder, drag and drop), previews, hot reload
3. PvP settings and HUD
4. UI customization
5. Profiles (save/switch/import/export), conflict view, starter packs
