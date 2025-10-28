# Development Guide - Common Library

This guide explains how to add new features, version the library, and deploy it for use in other projects.

## Table of Contents

1. [Adding New Features](#adding-new-features)
2. [Version Management](#version-management)
3. [Deployment Methods](#deployment-methods)
4. [Using the Library](#using-the-library)
5. [Git Workflow](#git-workflow)
6. [Best Practices](#best-practices)

---

## Adding New Features

### Step 1: Create Your Feature Branch

```bash
# Create and switch to feature branch
git checkout -b feature/add-new-utility

# Or for bug fixes
git checkout -b fix/issue-description
```

### Step 2: Add Your Code

#### Adding a New Utility Class

```bash
# Create your utility class in the appropriate package
src/main/java/com/lyhorng/common/util/YourNewUtil.java
```

Example:
```java
package com.lyhorng.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YourNewUtil {
    
    private YourNewUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    public static void someMethod(String param) {
        // Your code here
        log.info("Executing method with param: {}", param);
    }
}
```

#### Adding a New Configuration

```bash
# Create configuration class
src/main/java/com/lyhorng/common/config/YourConfig.java
```

#### Adding a New Response/Entity Class

```bash
# Create in appropriate package
src/main/java/com/lyhorng/common/response/YourResponse.java
# or
src/main/java/com/lyhorng/common/entity/YourEntity.java
```

### Step 3: Register Auto-Configuration (Optional)

If you want auto-configuration, add to `src/main/resources/META-INF/spring.factories`:

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.lyhorng.common.config.CommonConfig,\
com.lyhorng.common.config.RestTemplateConfig,\
com.lyhorng.common.config.JpaAuditingConfig,\
com.lyhorng.common.config.YourNewConfig
```

### Step 4: Update Documentation

Update `README.md` and `USAGE_EXAMPLES.md` with your new feature.

### Step 5: Build and Test

```bash
# Compile
mvn clean compile

# Run tests (if any)
mvn test

# Build with sources and javadoc
mvn clean install
```

### Step 6: Commit Your Changes

```bash
git add .
git commit -m "feat: add new utility class for specific functionality"

# Or for bug fixes
git commit -m "fix: resolve issue with date formatting"
```

---

## Version Management

### Semantic Versioning

Follow **MAJOR.MINOR.PATCH** convention:

- **MAJOR** - Breaking changes (e.g., 1.0.0 → 2.0.0)
- **MINOR** - New features, backward compatible (e.g., 1.0.0 → 1.1.0)
- **PATCH** - Bug fixes, backward compatible (e.g., 1.0.0 → 1.0.1)

### Update Version in pom.xml

```xml
<version>1.0.1</version>  <!-- Update this -->
```

### Version Update Checklist

- [ ] Update version in `pom.xml`
- [ ] Update CHANGELOG.md (create if doesn't exist)
- [ ] Update documentation if needed
- [ ] Test the build
- [ ] Commit version bump
- [ ] Tag the release

---

## Deployment Methods

You have 4 options for deploying your library:

### Method 1: Local Maven Repository (Simplest)

**Best for:** Internal use, team sharing, private development

**How others use it:**

```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.1</version>
</dependency>
```

**Steps:**
```bash
# Build the library
mvn clean install

# This installs to your local .m2 repository
# Location: ~/.m2/repository/com/lyhorng/common-lib/1.0.1/

# Others using the same machine automatically have access
```

**⚠️ Limitations:**
- Only accessible on the same machine
- Team members need to run `mvn install` locally
- Version conflicts possible

### Method 2: Private Git Repository + Local Install (Recommended for Private)

**Best for:** Team collaboration, version control, CI/CD

**Setup:**

1. Push to private Git repository:
```bash
git remote add origin git@github.com:your-org/common-lib.git
git push -u origin main
```

2. Team members clone and install:
```bash
# Each team member runs:
git clone git@github.com:your-org/common-lib.git
cd common-lib
mvn clean install
```

3. Use in other projects:
```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.1</version>
</dependency>
```

**⚠️ Requirements:**
- Everyone needs Git access to the repository
- Each person must run `mvn install` after pulling updates
- Works for private projects

### Method 3: Private Maven Repository (Nexus/Sonatype)

**Best for:** Enterprise, multiple teams, production use

**Setup:**

1. Configure in `pom.xml`:
```xml
<distributionManagement>
    <repository>
        <id>nexus-releases</id>
        <url>https://your-nexus-server.com/repository/maven-releases/</url>
    </repository>
    <snapshotRepository>
        <id>nexus-snapshots</id>
        <url>https://your-nexus-server.com/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

2. Deploy:
```bash
mvn clean deploy
```

3. Configure in client `pom.xml`:
```xml
<repositories>
    <repository>
        <id>nexus</id>
        <url>https://your-nexus-server.com/repository/maven-public/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.lyhorng</groupId>
        <artifactId>common-lib</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

**✅ Benefits:**
- Centralized storage
- Version management
- Authentication/authorization
- Works across organizations

### Method 4: GitHub Packages (Public/Private)

**Best for:** Open source or hosted private releases

**Setup:**

1. Add to `pom.xml`:
```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/your-org/common-lib</url>
    </repository>
</distributionManagement>
```

2. Create `.github/settings.xml` in your project or configure locally

3. Deploy:
```bash
export GITHUB_TOKEN=your_personal_access_token
mvn clean deploy
```

4. Configure client `pom.xml`:
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/your-org/*</url>
    </repository>
</repositories>
```

---

## Using the Library

### Scenario 1: Local Development (Team on Same Machines)

✅ **Can share if everyone runs:**
```bash
mvn clean install
```

This installs to shared `.m2` directory.

### Scenario 2: Private Git Repository

❌ **Users CANNOT automatically use it**

**Solution 1:** Each user installs locally
```bash
# User runs these commands:
git clone https://github.com/your-org/common-lib.git
cd common-lib
mvn clean install
```

**Solution 2:** Use as Git submodule
```bash
cd your-project
git submodule add https://github.com/your-org/common-lib.git libs/common-lib
cd libs/common-lib
mvn clean install -DskipTests
```

### Scenario 3: With Maven Repository (Nexus/GitHub Packages)

✅ **Users CAN use it directly**

Just add dependency in their `pom.xml`:
```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.1</version>
</dependency>
```

No extra setup needed!

---

## Git Workflow

### Standard Workflow

```bash
# 1. Create feature branch
git checkout -b feature/add-logging

# 2. Make changes
# ... edit files ...

# 3. Build and test
mvn clean install

# 4. Commit changes
git add .
git commit -m "feat: add advanced logging utility"

# 5. Push to remote
git push origin feature/add-logging

# 6. Create Pull Request (optional)
# Merge to main/master

# 7. Version bump
# Update pom.xml version
git add pom.xml
git commit -m "chore: bump version to 1.0.1"

# 8. Tag release
git tag -a v1.0.1 -m "Release version 1.0.1"
git push origin v1.0.1

# 9. Deploy (based on chosen method)
mvn clean install  # For local
# or
mvn clean deploy   # For remote repository
```

### Commit Message Format

Follow conventional commits:
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation changes
- `style:` - Code style changes
- `refactor:` - Code refactoring
- `test:` - Test changes
- `chore:` - Build/tooling changes

Examples:
```bash
git commit -m "feat: add retry mechanism for external API calls"
git commit -m "fix: handle null values in date parsing"
git commit -m "docs: update usage examples for new features"
```

---

## Best Practices

### 1. Version Management

**Always:**
- [ ] Update version in `pom.xml` before deploying
- [ ] Tag releases: `git tag v1.0.1`
- [ ] Update CHANGELOG.md
- [ ] Document breaking changes

**Version Bump Rules:**
```
1.0.0 → 1.0.1 (bug fix)
1.0.0 → 1.1.0 (new feature)
1.0.0 → 2.0.0 (breaking change)
```

### 2. Adding New Features

**Checklist:**
- [ ] Follow existing code style
- [ ] Add proper JavaDoc comments
- [ ] Handle null values safely
- [ ] Add to appropriate package
- [ ] Update documentation
- [ ] Test compilation: `mvn clean compile`
- [ ] Build artifacts: `mvn clean install`

### 3. Deployment

**Recommended Order:**
1. ✅ Local install for testing
2. ✅ Push to Git (private/public)
3. ✅ Deploy to Maven repository
4. ✅ Tag release

### 4. Security

**When using private libraries:**
- ✅ Require Git authentication
- ✅ Use private Maven repositories
- ✅ Control access via permissions
- ✅ Don't commit secrets/credentials

### 5. Documentation

**Always update:**
- README.md - Overview and quick start
- USAGE_EXAMPLES.md - Detailed examples
- CHANGELOG.md - Version history
- JavaDoc comments in code

---

## Troubleshooting

### Issue: "Cannot find artifact"

**Solution:**
```bash
# For users: Install the library locally
git clone <repository-url>
cd common-lib
mvn clean install

# Verify installation
ls ~/.m2/repository/com/lyhorng/common-lib/
```

### Issue: "Version not found"

**Solution:**
```bash
# Check available versions
ls ~/.m2/repository/com/lyhorng/common-lib/

# Install correct version
git checkout v1.0.1
mvn clean install
```

### Issue: "Dependency conflict"

**Solution:**
```xml
<!-- In client pom.xml, exclude conflicting dependency -->
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

## Summary

### Quick Decision Guide

**Can others use your private Git library?**

| Method | Private Git | Can Others Use? | Setup |
|--------|-------------|-----------------|-------|
| Local Maven | N/A | Only on same machine | `mvn install` |
| Git + Local Install | ✅ | ✅ Yes (after install) | Clone + `mvn install` |
| Private Maven Repo | ✅ | ✅ Yes (best) | Set up Nexus/GitHub Packages |
| GitHub Packages | ✅ | ✅ Yes | Configure in pom.xml |

**Recommendation:**
- **For teams:** Use Git + Local Install (Method 2)
- **For production:** Use Private Maven Repository (Method 3)
- **For open source:** Use GitHub Packages (Method 4)

---

## Next Steps

1. Review this guide
2. Choose deployment method
3. Set up your chosen method
4. Deploy your first version
5. Share with your team!

For questions or issues, check the main README.md or contact your team lead.

