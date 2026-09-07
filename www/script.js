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

    document.body.innerHTML = `
        <div class="overlay"></div>

        <main class="app">

            <header class="header">
                <h1>TRIM WORLD</h1>
                <p>Set your world boundaries</p>
            </header>

            <section class="menu">

                <div class="about-card">

                    <h2>Coordinates</h2>

                    <p class="description">
                        Enter two corner coordinates, similar to selecting
                        an area with Minecraft commands.
                    </p>

                    <hr>

                    <label>X1</label>
                    <input type="number" id="x1"
                        placeholder="Example: -1000">

                    <br><br>

                    <label>Z1</label>
                    <input type="number" id="z1"
                        placeholder="Example: -1000">

                    <br><br>

                    <label>X2</label>
                    <input type="number" id="x2"
                        placeholder="Example: 1000">

                    <br><br>

                    <label>Z2</label>
                    <input type="number" id="z2"
                        placeholder="Example: 1000">

                    <br><br>

                    <button class="menu-btn"
                        onclick="calculateTrim()">

                        📊 <span>Calculate Preview</span>

                    </button>

                    <button class="menu-btn"
                        onclick="goHome()">

                        ← <span>Back to Home</span>

                    </button>

                </div>

            </section>

        </main>
    `;
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

function calculateTrim() {

    const x1 = Number(document.getElementById("x1").value);
    const z1 = Number(document.getElementById("z1").value);

    const x2 = Number(document.getElementById("x2").value);
    const z2 = Number(document.getElementById("z2").value);

    if (
        document.getElementById("x1").value === "" ||
        document.getElementById("z1").value === "" ||
        document.getElementById("x2").value === "" ||
        document.getElementById("z2").value === ""
    ) {
        alert("❌ Please enter all coordinates.");
        return;
    }

    // Find minimum and maximum boundaries
    const minX = Math.min(x1, x2);
    const maxX = Math.max(x1, x2);

    const minZ = Math.min(z1, z2);
    const maxZ = Math.max(z1, z2);

    // Minecraft chunk coordinates
    const minChunkX = Math.floor(minX / 16);
    const maxChunkX = Math.floor(maxX / 16);

    const minChunkZ = Math.floor(minZ / 16);
    const maxChunkZ = Math.floor(maxZ / 16);

    // Number of chunks to keep
    const chunksX =
        maxChunkX - minChunkX + 1;

    const chunksZ =
        maxChunkZ - minChunkZ + 1;

    const totalChunks =
        chunksX * chunksZ;

    alert(
        "📊 TRIM PREVIEW\n\n" +

        "World area to KEEP:\n" +
        "X: " + minX + " → " + maxX + "\n" +
        "Z: " + minZ + " → " + maxZ + "\n\n" +

        "Chunk range:\n" +
        "X: " + minChunkX + " → " + maxChunkX + "\n" +
        "Z: " + minChunkZ + " → " + maxChunkZ + "\n\n" +

        "Chunks to keep: " + totalChunks
    );
}
