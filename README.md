# RAW PIXELS

Raw Pixels is an Android application for making graffiti handstyles.

I'm a graffiti writer and a graphic designer, painting since 2008, on Linux. Not a programmer, ateam or a company, so feedback is very appreciated!

## Download

    file            Raw-Pixels-2.5.apk
    sha256          b25f0c1072a57b7284de7c463e88cdebc55378f27b6a0ce01bfef46c6fd2d954
    virustotal      https://www.virustotal.com/gui/file/b25f0c1072a57b7284de7c463e88cdebc55378f27b6a0ce01bfef46c6fd2d954
    web version     https://nknw.itch.io/raw-pixels
    page            https://nknwlab.com/rawpixels

The sha256 is the fingerprint of that exact file. Verify it with `sha256sum -c SHA256SUMS`.

## Source Code & Transparency

The source code is published strictly for transparency and security verification. Anyone is free to inspect the repository to confirm that the application is clean, secure, and entirely free of telemetry, spyware, or third-party tracking.

## Ownership & Strict Usage Terms

While I believe in open-source transparency, standard permissive licenses do not prevent corporate entities, third parties, or opportunistic developers from taking code, rebranding it, monetizing it, or running ad-driven campaigns under the guise of modern marketing strategies.

To prevent corporate exploitation and unauthorized commercial derivative works, this repository operates under a custom Source-Available License.

What is allowed: personal inspection, security auditing, local private customization, using the application for personal creative output, sharing/demonstrating the application, submitting bug fixes or patches to the repository, and modifying it for yourself and for your friends. If you publish the modified build, write to NKNW Lab first and keep the NKNW Lab logo in the app. Your name can sit next to it, like a collab.

What is strictly prohibited:
- Commercial exploitation, resale, corporate rebranding, claiming intellectual ownership, or public redistribution of derivative builds.
- Modifying the application to inject advertisements, tracking mechanisms, or third-party analytics.
- Automated Code Extraction & AI Reverse-Engineering: using, scraping, or feeding this codebase into automated AI models or automated synthesis tools to reverse-engineer, replicate, or generate derivative software for private or commercial deployment.

The images, backgrounds, tools and templates are made by NKNW Lab. They are part of the app, not a free asset pack: same rules as the code. The bundled fonts are Google Fonts, SIL Open Font License or Apache 2.0, and follow their own licence.

All modifications or derivative uses must retain explicit credit to NKNW Lab. The code is open for visibility and security verification, but intellectual property and ownership rights remain strictly private and protected.

## Layout

`www/` is the app: plain HTML, CSS and JS, no framework and no bundler.
`android/` is the Capacitor project. The APK is built from `www/`.
`fonts/` holds Press Start 2P (SIL OFL).
The copies in the root (`index.html`, `Tags.html`, `images/`) are manual backups. The build only reads `www/`.

## Build
Gradle
AndroidStudio


## License

NKNW_LAB — Source-Available License. Copyright (c) NKNW Lab. All rights reserved.
Terms above. No warranty, provided as is.
static.void14@gmail.com
