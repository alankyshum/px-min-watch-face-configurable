# Clean-room provenance

This repository was initialized as a new Git repository. The private behavioral reference was read only; no files, Git objects, commits, images, or font artifacts were copied from it. WFF XML was authored anew after consulting the public Android WFF documentation and public `android/wear-os-samples` structure. Generic Gradle and Android manifest conventions are interoperable boilerplate, not expressive design material.

Allowed tracked binary artifacts are the official, unmodified Orbitron variable font (fully identified in `THIRD_PARTY_NOTICES.md`) and the Gradle wrapper JAR. The provider icon is an authored XML vector. There are no tracked screenshots, APKs, keys, device logs, calendar data, or private-reference assets.

The CI `provenance` job rejects unexpected tracked binary extensions and scans tracked content for forbidden private package identifiers and Google Sans naming.
