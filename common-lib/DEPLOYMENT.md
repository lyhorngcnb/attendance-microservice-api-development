# Deployment Guide - Common Library

This guide provides step-by-step instructions for deploying the common library for use in other projects.

## Quick Start

### Option 1: Quick Local Deployment (5 minutes)

```bash
# In your common-lib directory
mvn clean install

# Done! Use in other projects:
```

```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## Detailed Deployment Scenarios

### Scenario A: Private Git Repository (Recommended for Teams)

**Question:** "I have a private Git repo, can my team use it?"

**Answer:** ✅ **YES, but each person needs to install it locally**

#### Step-by-Step Instructions

**For You (Library Maintainer):**

```bash
# 1. Build the library
cd common-lib
mvn clean install

# 2. Push to Git
git add .
git commit -m "chore: bump version to 1.0.1"
git tag v1.0.1
git push origin main
git push origin v1.0.1
```

**For Your Team Members:**

```bash
# 1. Clone the repository (one-time setup)
git clone git@github.com:your-org/common-lib.git
cd common-lib

# 2. Install to local Maven repository (or run this after each update)
mvn clean install

# 3. Use in their projects
cd ~/projects/my-api
# Add dependency to pom.xml
```

**Client Project Setup:**

```xml
<!-- In client pom.xml -->
<dependencies>
    <dependency>
        <groupId>com.lyhorng</groupId>
        <artifactId>common-lib</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

**✅ Pros:**
- Version control via Git
- Team can collaborate
- Private and secure
- Free

**❌ Cons:**
- Each developer must run `mvn install`
- Manual version updates

---

### Scenario B: Maven Repository (Nexus/Auth Server)

**Question:** "Can I deploy to a private Maven server?"

**Answer:** ✅ **YES, then your team can use it like any other Maven dependency!**

#### Setup Instructions

**1. Configure Library (`pom.xml`):**

```xml
<distributionManagement>
    <repository>
        <id>nexus-releases</id>
        <name>Releases</name>
        <url>https://your-nexus-server.com/repository/maven-releases/</url>
    </repository>
    <snapshotRepository>
        <id>nexus-snapshots</id>
        <name>Snapshots</name>
        <url>https://your-nexus-server.com/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

**2. Configure Credentials (`~/.m2/settings.xml`):**

```xml
<settings>
    <servers>
        <server>
            <id>nexus-releases</id>
            <username>your-username</username>
            <password>your-password</password>
        </server>
        <server>
            <id>nexus-snapshots</id>
            <username>your-username</username>
            <password>your-password</password>
        </server>
    </servers>
</settings>
```

**3. Deploy:**

```bash
# Build and deploy
mvn clean deploy

# For snapshots
mvn clean deploy -DskipTests
```

**4. Client Project Setup:**

```xml
<!-- Add repository to client pom.xml -->
<repositories>
    <repository>
        <id>nexus</id>
        <url>https://your-nexus-server.com/repository/maven-public/</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<!-- Add dependency -->
<dependencies>
    <dependency>
        <groupId>com.lyhorng</groupId>
        <artifactId>common-lib</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

**✅ Pros:**
- Centralized management
- Automatic dependency resolution
- No local installation needed
- Professional setup

**❌ Cons:**
- Requires server setup
- May have costs

---

### Scenario C: GitHub Packages (Free for Private/Public)

**Question:** "Can I use GitHub to host my Maven package?"

**Answer:** ✅ **YES! GitHub Packages is free for private repos**

#### Setup Instructions

**1. Configure Library (`pom.xml`):**

```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/YOUR_GITHUB_USERNAME/common-lib</url>
    </repository>
</distributionManagement>
```

**2. Create GitHub Personal Access Token:**

- Go to: https://github.com/settings/tokens
- Create token with `write:packages` permission
- Save token securely

**3. Configure Credentials (`~/.m2/settings.xml`):**

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

**4. Deploy:**

```bash
mvn clean deploy
```

**5. Client Project Setup:**

```xml
<!-- Add repository -->
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/YOUR_GITHUB_USERNAME/*</url>
    </repository>
</repositories>

<!-- Add dependency -->
<dependencies>
    <dependency>
        <groupId>com.lyhorng</groupId>
        <artifactId>common-lib</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

**✅ Pros:**
- Free for private repos
- Integrated with Git
- Version management
- Professional

**❌ Cons:**
- Need to manage tokens
- Slightly more setup

---

## Version Management Workflow

### Complete Deployment Workflow

```bash
# 1. Make your changes and test
git checkout -b feature/add-new-feature
# ... make changes ...
mvn clean compile
mvn test

# 2. Update version in pom.xml
# Change version from 1.0.0 to 1.0.1

# 3. Commit and push
git add .
git commit -m "feat: add new feature"
git push origin feature/add-new-feature

# 4. Merge to main (via Pull Request or direct)
git checkout main
git merge feature/add-new-feature

# 5. Tag the release
git tag -a v1.0.1 -m "Release version 1.0.1"
git push origin v1.0.1

# 6. Deploy (choose method)

# Method A: Local (for team using local install)
mvn clean install

# Method B: Maven Repository
mvn clean deploy

# Method C: GitHub Packages
mvn clean deploy

# 7. Update client projects to new version
# Change version in their pom.xml: 1.0.0 → 1.0.1
mvn dependency:update
```

---

## Common Issues & Solutions

### Issue 1: "Cannot resolve dependency"

**Error:**
```
Could not find artifact com.lyhorng:common-lib:jar:1.0.1
```

**Solution:**

```bash
# Make sure library is installed locally
cd common-lib
mvn clean install

# Or if using Maven repository, check credentials
# Verify ~/.m2/settings.xml has correct credentials
```

### Issue 2: "Wrong version available"

**Error:**
```
Could not find version '1.0.2'
```

**Solution:**

```bash
# Check available versions
ls ~/.m2/repository/com/lyhorng/common-lib/

# Install the correct version
git checkout v1.0.2
mvn clean install

# Or update client to use available version
```

### Issue 3: "Authentication failed"

**Error:**
```
[ERROR] Failed to execute goal ... Authentication failed
```

**Solution:**

```bash
# Check credentials in ~/.m2/settings.xml
# Verify username/password/token

# For GitHub Packages, regenerate token
# For Nexus, contact administrator
```

### Issue 4: "Dependency conflicts"

**Error:**
```
Conflicts between versions of org.springframework:spring-web
```

**Solution:**

```xml
<!-- Exclude conflicting dependency -->
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.1</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

---

## Comparison Table

| Method | Setup Time | Cost | Team Size | Best For |
|--------|-----------|------|-----------|----------|
| Local Maven | 1 min | Free | 1-3 | Quick testing |
| Git + Local Install | 5 min | Free | 3-10 | Small teams |
| Maven Repository | 30 min | $ (server) | 10+ | Large teams |
| GitHub Packages | 15 min | Free | Any | Any size |

---

## Recommendations

### For Solo Developer
1. ✅ Use Local Maven (Method A)
2. ✅ Simple, fast, no setup

### For Small Team (2-10 people)
1. ✅ Use Git + Local Install (Method B)
2. ✅ Free, version controlled
3. ✅ Each developer runs `mvn install` after pulling

### For Large Team (10+ people)
1. ✅ Use Maven Repository (Method C)
2. ✅ Centralized, professional
3. ✅ No local installation needed

### For Open Source Project
1. ✅ Use GitHub Packages
2. ✅ Free, version control
3. ✅ Public or private

---

## Quick Reference

### Build & Install Locally
```bash
mvn clean install
# Installs to: ~/.m2/repository/com/lyhorng/common-lib/
```

### Deploy to Repository
```bash
mvn clean deploy
# Requires configured distributionManagement
```

### Check Installed Version
```bash
ls ~/.m2/repository/com/lyhorng/common-lib/
```

### Update Version in pom.xml
```xml
<version>1.0.2</version>
```

### Tag Release
```bash
git tag -a v1.0.2 -m "Release version 1.0.2"
git push origin v1.0.2
```

---

## Support

For issues or questions:
- Check DEVELOPMENT_GUIDE.md for adding features
- Check README.md for usage
- Check USAGE_EXAMPLES.md for code examples

