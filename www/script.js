let selectedWorld = null;

/* =========================
   SELECT & VERIFY WORLD
========================= */

async function selectWorld() {

    try {

        const plugin =
            window.Capacitor?.Plugins?.TrimPlugin;

        if (!plugin) {
            alert("❌ TrimPlugin not found.");
            return;
        }

        // Open Android native folder picker
        const selection =
            await plugin.selectWorld();

        if (!selection || !selection.uri) {
            alert("❌ No folder selected.");
            return;
        }

        // Save selected world
        selectedWorld = {
            uri: selection.uri
        };

        // Verify Minecraft world
        const verification =
            await plugin.verifyWorld({
                uri: selectedWorld.uri
            });

        if (verification.validWorld) {

            alert(
                "✅ Valid Minecraft Bedrock World!\n\n" +
                "level.dat: Found ✅\n" +
                "db folder: Found ✅"
            );

        } else {

            alert(
                "❌ Invalid Minecraft World!\n\n" +
                "This folder does not contain the required Minecraft world files.\n\n" +
                "level.dat: " +
                (verification.hasLevelDat ? "Found ✅" : "Not Found ❌") +
                "\n" +
                "db folder: " +
                (verification.hasDbFolder ? "Found ✅" : "Not Found ❌")
            );

            selectedWorld = null;
        }

    } catch (error) {

        alert(
            "❌ World selection failed:\n\n" +
            String(error)
        );
    }
}


/* =========================
   OLD FILE PICKER
   (Not used now)
========================= */

function worldSelected(input) {
    // Native Android folder picker is now used.
}


/* =========================
   TRIM WORLD
========================= */

function trimWorld() {

    if (!selectedWorld) {
        alert("❌ Please select a valid Minecraft world first.");
        return;
    }

    alert(
        "✂️ Trim World\n\n" +
        "World selected successfully!\n\n" +
        "The coordinate trim system will be added next."
    );
}


/* =========================
   BACKUP
========================= */

function backupWorld() {

    if (!selectedWorld) {
        alert("❌ Please select a valid Minecraft world first.");
        return;
    }

    alert(
        "💾 Backup\n\n" +
        "Backup system will be connected to the selected world next."
    );
}


/* =========================
   SETTINGS
========================= */

function openSettings() {

    document.body.innerHTML = `
        <div class="overlay"></div>

        <main class="app">

            <header class="header">
                <h1>SETTINGS</h1>
                <p>Trim PE</p>
            </header>

            <section class="menu">

                <div class="about-card">

                    <h2>Settings</h2>

                    <p class="description">
                        Trim PE settings and preferences.
                    </p>

                    <hr>

                    <p>
                        ⚠️ Always create a backup before trimming a world.
                    </p>

                    <hr>

                    <button class="menu-btn" onclick="goHome()">
                        ← <span>Back to Home</span>
                    </button>

                </div>

            </section>

        </main>
    `;
}


/* =========================
   ABOUT
========================= */

function openAbout() {

    document.body.innerHTML = `
        <div class="overlay"></div>

        <main class="app">

            <header class="header">
                <h1>ABOUT</h1>
                <p>Trim PE</p>
            </header>

            <section class="menu">

                <div class="about-card">

                    <h2>Trim PE</h2>

                    <p class="description">
                        Trim PE is a simple and easy-to-use tool
                        designed for Minecraft Bedrock and Pocket
                        Edition players.
                    </p>

                    <hr>

                    <p><strong>Developer</strong></p>
                    <p>WindX</p>

                    <hr>

                    <button class="menu-btn" onclick="openDiscord()">
                        💬 <span>Discord: windx.io</span>
                    </button>

                    <button class="menu-btn" onclick="openDiscordServer()">
                        🌐 <span>Discord Server</span>
                    </button>

                    <button class="menu-btn" onclick="openGithub()">
                        🐙 <span>GitHub</span>
                    </button>

                    <button class="menu-btn" onclick="goHome()">
                        ← <span>Back to Home</span>
                    </button>

                </div>

            </section>

        </main>
    `;
}


/* =========================
   LINKS
========================= */

function openDiscord() {
    alert("Discord username: windx.io");
}


function openDiscordServer() {
    window.open(
        "https://discord.gg/RjYR6vVjQw",
        "_blank"
    );
}


function openGithub() {
    window.open(
        "https://github.com/jdjauss-ship-it",
        "_blank"
    );
}


function goHome() {
    location.reload();
}


/* =========================
   NATIVE CONNECTION TEST
========================= */

async function testNativeConnection() {

    try {

        const plugin =
            window.Capacitor?.Plugins?.TrimPlugin;

        if (!plugin) {
            alert("❌ TrimPlugin not found.");
            return;
        }

        const result =
            await plugin.testConnection();

        alert("✅ " + result.message);

    } catch (error) {

        alert(
            "❌ Native connection failed:\n\n" +
            String(error)
        );
    }
               }
