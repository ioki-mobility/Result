## 📦 How to Release 

### 1. Check out the `main` Branch

   - Ensure you are on the `main` branch and have pulled the latest changes:
     ```bash
     git checkout main
     git pull origin main
     ```

### 2. Update the Version in `library/build.gradle.kts`

   - Remove `-SNAPSHOT` from the `version` in [`library/build.gradle.kts`](library/build.gradle.kts).
   - Commit the change:
     ```bash
     git commit -m "Prepare next release" .
     ```

### 3. Create a Git Tag and Push It

   - Tag the release version and push it:
     ```bash
     git tag x.y.z
     git push origin x.y.z
     ```

### 4. Trigger the Release Action

   - This will kick off a [GitHub action (`release.yml`)](../../actions/workflows/release.yml) to release the library to a [Sonatype](https://s01.oss.sonatype.org/) staging repository. 📤
   - The action will also draft a [new release on GitHub](../../releases).

### 5. Sign in to Sonatype and Close the Staged Repo

   - Head to [Sonatype](https://s01.oss.sonatype.org/) and sign in (credentials are in the Bitwarden Open Source Collection).
   - Search for the staged repo, click **Close**.
   - After it's successfully **Closed**, click **Release** and check "Drop after release".

### 6. Edit the Release on GitHub

   - After step 4 is complete, head to the [GitHub release page](../../releases) and edit the new release, and publish it. 🎉

### 7. Bump the Version for Development

   - Increment the **minor** (Major.Minor.Patch) version in [`library/build.gradle.kts`](library/build.gradle.kts) and add `-SNAPSHOT` to indicate the next development version.
   - Commit this change:
     ```bash
     git commit -m "Next development version" .
     ```

### 8. Push and Celebrate! 🎉💃

   - Push your changes and celebrate a job well done!
