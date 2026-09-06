function selectWorld() {
    document.getElementById("worldPicker").click();
}

function worldSelected(input) {
    const files = input.files;

    if (!files || files.length === 0) {
        return;
    }

    const firstFile = files[0];

    alert(
        "World selected successfully!\n\n" +
        "Files found: " + files.length + "\n" +
        "First file: " + firstFile.name
    );
}

function trimWorld() {
alert("Please select a Minecraft world first.");
}

function backupWorld() {
alert("Backup feature will be added soon!");
}

function openSettings() {
alert("Settings will be available soon!");
}

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
                    Trim PE is a simple and easy-to-use tool designed for
                    Minecraft Bedrock and Pocket Edition players. It helps
                    manage and trim Minecraft worlds with a clean, modern,
                    and user-friendly experience.
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

function openDiscord() {
alert("Discord username: windx.io");
}

function openDiscordServer() {
window.open("https://discord.gg/RjYR6vVjQw", "_blank");
}

function openGithub() {
window.open("https://github.com/jdjauss-ship-it", "_blank");
}

function goHome() {
location.reload();
}
