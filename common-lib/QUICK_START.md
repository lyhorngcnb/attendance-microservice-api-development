# Quick Start Guide

Get up and running with the Common Library in 5 minutes!

## Table of Contents

1. [Using the Library](#using-the-library)
2. [Adding New Features](#adding-new-features)
3. [Deploying New Versions](#deploying-new-versions)
4. [FAQ](#faq)

---

## Using the Library

### Step 1: Add Dependency

In your project's `pom.xml`:

```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Step 2: Enable Auto-Configuration

```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.lyhorng.common", "your.package"})
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### Step 3: Start Using!

```java
// Use in controllers
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ApiResponse.success(user);
    }
}

// Use utilities
import com.lyhorng.common.util.StringUtils;

if (StringUtils.isValidEmail(email)) {
    // Valid email
}

// Use validation
import com.lyhorng.common.util.ValidationUtils;

ValidationUtils.notNull(obj, "Object cannot be null");
```

**Done!** The library is now integrated.

---

## Adding New Features

### Quick Process (3 steps)

1. **Create your feature:**
```bash
# Create new utility
src/main/java/com/lyhorng/common/util/YourUtil.java
```

2. **Build:**
```bash
mvn clean install
```

3. **Use it:**
```java
YourUtil.someMethod();
```

### Example: Adding a New Utility

**File:** `src/main/java/com/lyhorng/common/util/TimeUtils.java`

```java
package com.lyhorng.common.util;

public class TimeUtils {
    
    private TimeUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
}
```

**Build and use:**
```bash
mvn clean install
```

```java
import com.lyhorng.common.util.TimeUtils;

long timestamp = TimeUtils.getCurrentTimestamp();
```

**For detailed instructions, see [DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md)**

---

## Deploying New Versions

### Scenario 1: Private Git Repository (Most Common)

**You have a private Git repo, your team wants to use it:**

#### Setup (One-time):

**For each team member:**
```bash
# Clone repository
git clone git@github.com:your-org/common-lib.git
cd common-lib

# Install to local Maven repository
mvn clean install

# Done! Now they can use it in their projects
```

**Use in projects:**
```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

**When you add new features:**
```bash
# Update library
git pull origin main
mvn clean install

# Use new version in client projects
# Change version in pom.xml if needed
```

#### You (Library Maintainer):

```bash
# 1. Make changes
git checkout -b feature/add-new-util
# ... add your code ...

# 2. Update version in pom.xml
# <version>1.0.1</version>

# 3. Build and test
mvn clean install

# 4. Commit and tag
git add .
git commit -m "feat: add new utility"
git push origin feature/add-new-util

# 5. Merge to main
git checkout main
git merge feature/add-new-util

# 6. Tag release
git tag -a v1.0.1 -m "Release version 1.0.1"
git push origin v1.0.1

# 7. Tell team to update
# They run: git pull && mvn clean install
```

**✅ Can others use your private Git library?**

**YES!** As long as they have:
- Git access to the repository
- Run `mvn clean install` locally
- Same local Maven repository (`~/.m2`)

### Scenario 2: Using with Maven Repository (Recommended for Teams)

**Even better option:**

#### Setup Maven Repository (One-time, ask admin):

```bash
# Configure in pom.xml
<distributionManagement>
    <repository>
        <id>nexus-releases</id>
        <url>https://your-maven-server.com/repository/releases/</url>
    </repository>
</distributionManagement>
```

#### Deploy:

```bash
mvn clean deploy
```

#### Team can use it directly:

```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.1</version>
</dependency>
```

**No local installation needed!**

**For complete deployment guide, see [DEPLOYMENT.md](DEPLOYMENT.md)**

---

## FAQ

### Q: Can others use my private Git library?

**A: YES!** They need to:
1. Clone your private repository (need access)
2. Run `mvn clean install` locally
3. Use the dependency in their projects

Your private Git repository is private, but once someone clones it and installs it locally, they can use it.

### Q: Do I need to setup a Maven repository?

**A: Not required!** Options:
1. ✅ Private Git + Local Install (FREE, easy)
2. ✅ Maven Repository (Professional, requires server)
3. ✅ GitHub Packages (FREE, requires setup)

### Q: How do I add new features?

**A: Simple 3-step process:**
1. Create new file in appropriate package
2. Run `mvn clean install`
3. Use it in your projects

See [DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md) for details.

### Q: How do I deploy new versions?

**A: Update version and build:**
```bash
# 1. Update version in pom.xml
<version>1.0.1</version>

# 2. Build
mvn clean install

# 3. Tag release
git tag v1.0.1
git push origin v1.0.1
```

### Q: My team can't find the dependency?

**A: They need to install it locally:**
```bash
git clone <your-repo>
cd common-lib
mvn clean install
```

### Q: How do I update to latest version?

**A: Pull and install:**
```bash
cd common-lib
git pull
mvn clean install

# Update version in your project's pom.xml if needed
```

### Q: Can I use this without Spring Boot?

**A: Partially.** Utilities work standalone, but configuration classes require Spring Boot. You can use individual utility classes without Spring.

---

## Comparison: Deployment Methods

| Method | Setup Time | Cost | Best For |
|--------|-----------|------|----------|
| **Git + Local Install** | 5 min | Free | Small teams (3-10) |
| **Maven Repository** | 30 min | $ | Large teams (10+) |
| **GitHub Packages** | 15 min | Free | Any team size |

**Recommendation:** Start with Git + Local Install, upgrade to Maven Repository when team grows.

---

## Next Steps

- 📖 [README.md](README.md) - Full documentation
- 💡 [USAGE_EXAMPLES.md](USAGE_EXAMPLES.md) - Code examples
- 🛠️ [DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md) - Adding features
- 🚀 [DEPLOYMENT.md](DEPLOYMENT.md) - Deployment guide

Need help? Check the documentation or contact your team lead!

