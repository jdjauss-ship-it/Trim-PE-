let selectedWorld = null;

/* =========================
   SELECT WORLD
========================= */

function selectWorld() {
    document.getElementById("worldPicker").click();
}

function worldSelected(input) {

    const files = input.files;

    if (!files || files.length === 0) return;

    let worldName = files[0].name;

    if (files[0].webkitRelativePath) {
        worldName =
            files[0].webkitRelativePath.split("/")[0];
    }

    selectedWorld = {
        name: worldName,
        files: files
    };

    alert(
        "World selected successfully!\n\n" +
        "World: " + selectedWorld.name + "\n" +
        "Files found: " + files.length
    );
}


/* =========================
   TRIM WORLD
========================= */

function trimWorld() {

    if (!selectedWorld) {
        alert("Please select a Minecraft world first.");
        return;
    }

    alert(
        "Trim World\n\n" +
        "Selected World: " + selectedWorld.name +
        "\n\nTrim Engine will be connected next."
    );
}


/* =========================
   BACKUP
========================= */

function backupWorld() {

    if (!selectedWorld) {
        alert("Please select a Minecraft world first.");
        return;
    }

    alert(
        "Backup World\n\n" +
        "Selected World: " + selectedWorld.name +
        "\n\nBackup system will be connected next."
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
                        Edition players. It helps manage Minecraft
                        worlds with a clean and user-friendly
                        experience.
                    </p>

                    <hr>

                    <p><strong>Developer</strong></p>

                    <p>WindX</p>

                    <hr>

                    <button class="menu-btn"
                        onclick="openDiscord()">

                        💬 <span>Discord: windx.io</span>

                    </button>

                    <button class="menu-btn"
                        onclick="openDiscordServer()">

                        🌐 <span>Discord Server</span>

                    </button>

                    <button class="menu-btn"
                        onclick="openGithub()">

                        🐙 <span>GitHub</span>

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
   NATIVE ANDROID TEST
========================= */

async function testNativeConnection() {

    try {

        const plugin =
            window.Capacitor?.Plugins?.TrimPlugin;

        if (!plugin) {

            alert(
                "❌ TrimPlugin not found."
            );

            return;
        }

        const result =
            await plugin.testConnection();

        alert(
            "✅ " + result.message
        );

    } catch (error) {

        alert(
            "❌ Native connection failed:\n\n" +
            String(error)
        );

    }
}
