# Guide: Local Maven Setup with SDKMAN!

This guide summarizes how to install and configure a local version of Apache Maven on macOS using SDKMAN! and how to configure IntelliJ IDEA and VS Code to use it.

## Why Install Maven Locally?

Even though IDEs like IntelliJ and VS Code bundle their own Maven versions, installing it locally provides several key benefits:

* **Consistency:** Guarantees that builds run the same way in your IDE, in your terminal, and on your CI/CD server.
* **Command-Line Power:** Allows you to run `mvn` commands from any terminal, which is essential for scripting and simulating production builds.
* **Centralized Configuration:** Uses a single, global `~/.m2/settings.xml` file for all tools, managing proxies, private repositories, and credentials in one place.
* **Explicit Version Control:** You control exactly which version of Maven is used for your projects, which is critical for compatibility.

## Step 1: Install SDKMAN!

SDKMAN! is a tool for managing parallel versions of multiple Software Development Kits, including Maven and Java.

1.  **Install SDKMAN!:**
    ```bash
    curl -s "[https://get.sdkman.io](https://get.sdkman.io)" | bash
    ```

2.  **Activate SDKMAN!** (Run this or restart your terminal):
    ```bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"
    ```

3.  **Verify:**
    ```bash
    sdk version
    ```

## Step 2: Install Maven using SDKMAN!

1.  **Install the latest stable version of Maven:**
    ```bash
    sdk install maven
    ```
    *Alternatively, to install a specific version:*
    ```bash
    # sdk install maven 3.8.8
    ```

2.  **Verify the Maven installation:**
    ```bash
    mvn -v
    ```
    SDKMAN! automatically handles setting the `PATH` and `M2_HOME` environment variables for you.

## Step 3: Maven Configuration (The `settings.xml` file)

SDKMAN! installs Maven, but you still manage its *configuration*. Maven's global configuration is managed in the `settings.xml` file.

* **File Location:** `~/.m2/settings.xml`
    *(Full path: `/Users/YourUserName/.m2/settings.xml`)*
* **Action:** This file is **not** created by default. You must create the `~/.m2` directory and the `settings.xml` file yourself to add custom configurations like proxy servers or private repository credentials.

## Step 4: Configure Your IDEs

Point your IDEs to the Maven installation managed by SDKMAN! to ensure consistency.

### Key Path Information

The path to your SDKMAN!-managed Maven is:
`/Users/YourUserName/.sdkman/candidates/maven/current`

*(Replace `YourUserName` with your actual home folder name. Run `whoami` in the terminal if you are unsure.)*

---

### IntelliJ IDEA Configuration

1.  Go to **Preferences** (`Cmd + ,`) > **Build, Execution,Deployment** > **Build Tools** > **Maven**.
2.  Find the **Maven home path** setting.
3.  Click the dropdown, and paste the path to your SDKMAN! Maven **home directory**:
    ```
    /Users/YourUserName/.sdkman/candidates/maven/current
    ```
4.  Click **Apply** and **OK**.

---

### Visual Studio Code (VS Code) Configuration

1.  Open the Command Palette (`Cmd + Shift + P`).
2.  Search for and select **Preferences: Open User Settings (JSON)**.
3.  Add the following line to your `settings.json` file, pointing to the `mvn` **executable file**:
    {
      // ... your other settings
      "maven.executable.path": "/Users/YourUserName/.sdkman/candidates/maven/current/bin/mvn"
    }
4.  Save the `settings.json` file.