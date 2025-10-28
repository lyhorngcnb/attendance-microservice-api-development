# Real Workflow Example

Let me show you exactly what happens with a complete example.

## 📦 Your Library Setup

**Location:** `https://github.com/your-org/common-lib` (private repo)  
**Current version:** 1.0.0

---

## Scenario 1: Your Team Member Wants to Use It

### Step 1: They Clone Your Repository

```bash
# Team member runs:
git clone git@github.com:your-org/common-lib.git
cd common-lib

# They now have your source code
```

### Step 2: They Install It Locally

```bash
# Build and install to local Maven repository
mvn clean install

# Output:
# [INFO] Building JAR: common-lib-1.0.0.jar
# [INFO] Installing to: ~/.m2/repository/com/lyhorng/common-lib/1.0.0/
```

**✅ Now the JAR file is installed on their computer!**

### Step 3: They Use It in Their Project

**Their project:** `MyAwesomeAPI`

**In their `pom.xml`:**
```xml
<dependencies>
    <dependency>
        <groupId>com.lyhorng</groupId>
        <artifactId>common-lib</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

**In their Java code:**
```java
package com.example.api.controller;

import com.lyhorng.common.response.ApiResponse;
import com.lyhorng.common.util.StringUtils;
import com.lyhorng.common.util.ValidationUtils;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        
        // ✅ Using YOUR library method!
        return ApiResponse.success(user);
    }
    
    @PostMapping
    public ApiResponse<User> createUser(@RequestBody UserRequest request) {
        // ✅ Using YOUR validation
        ValidationUtils.notNull(request, "Request cannot be null");
        ValidationUtils.validEmail(request.getEmail(), "Invalid email");
        
        // ✅ Using YOUR utilities
        String normalized = StringUtils.normalizeWhitespace(request.getName());
        
        User user = userService.create(request);
        
        // ✅ Using YOUR response wrapper
        return ApiResponse.success("User created", user);
    }
}
```

**✅ They're now using YOUR library!**

---

## Scenario 2: You Add a New Feature

### You Add a New Method

**File:** `StringUtils.java`

```java
public class StringUtils {
    
    // NEW METHOD YOU ADDED:
    public static String slugify(String text) {
        if (isEmpty(text)) return "";
        return text.toLowerCase()
                  .replaceAll("[^a-z0-9\\s-]", "")
                  .replaceAll("\\s+", "-");
    }
}
```

### You Build and Deploy

```bash
# 1. Update version
# In pom.xml: <version>1.1.0</version>

# 2. Build
mvn clean install

# 3. Commit and push
git add .
git commit -m "feat: add slugify method"
git tag v1.1.0
git push origin main
git push origin v1.1.0
```

### Team Member Updates

```bash
cd common-lib
git pull origin main        # Gets your new code
mvn clean install            # Builds new version
```

**Now they can use your new method:**
```java
String slug = StringUtils.slugify("Hello World! #123");
// Result: "hello-world-123"
```

---

## Visual Example

### What You Have:

```
your-git-repo/
├── src/main/java/com/lyhorng/common/
│   ├── util/
│   │   ├── StringUtils.java     ← Your code
│   │   ├── DateUtils.java       ← Your code
│   │   └── ValidationUtils.java ← Your code
│   └── response/
│       └── ApiResponse.java      ← Your code
└── pom.xml
```

### After Team Member Installs:

```
their-computer/
└── ~/.m2/repository/com/lyhorng/common-lib/
    └── 1.0.0/
        ├── common-lib-1.0.0.jar           ← Built JAR
        ├── common-lib-1.0.0-sources.jar  ← Source JAR
        └── pom.xml
```

### Their Project Uses It:

```java
// Their code
import com.lyhorng.common.util.StringUtils;  ← From YOUR JAR
import com.lyhorng.common.response.ApiResponse; ← From YOUR JAR

// Using YOUR methods
ApiResponse.success(user);
StringUtils.isValidEmail(email);
```

---

## Full Complete Example

### Day 1: Team Member Sets Up

```bash
# 1. Clone your library
git clone git@github.com:your-org/common-lib.git
cd common-lib

# 2. Install it
mvn clean install
# Done! Library is ready
```

### Day 2: Team Member Uses It

**In their project:**
```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java
// Their code
public class OrderService {
    public void process(OrderRequest request) {
        // ✅ Using YOUR validation
        ValidationUtils.notNull(request, "Request required");
        
        // ✅ Using YOUR utilities
        String cleanName = StringUtils.normalizeWhitespace(request.getName());
        
        // ✅ Using YOUR response
        return ApiResponse.success("Order processed", order);
    }
}
```

### Day 3: You Add New Feature

**You:**
```bash
# Add new method to StringUtils
# Build and push
mvn clean install
git add . && git commit -m "feat: add new method"
git push
```

**Team Member:**
```bash
cd common-lib
git pull        # Get your new code
mvn install     # Build new version
```

**Now they can use your new method:**
```java
String newResult = StringUtils.yourNewMethod();
```

---

## Key Understanding

### The Dependency You Show:

```xml
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

**What this means:**
- ✅ Maven looks for: `~/.m2/repository/com/lyhorng/common-lib/1.0.0/`
- ✅ It finds the JAR file you built with `mvn clean install`
- ✅ It can use your classes and methods

### Without `mvn clean install`:

```xml
<!-- This won't work! -->
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

❌ **Error:** "Cannot find artifact"

### With `mvn clean install`:

```bash
# This makes it work!
mvn clean install
```

✅ **Now it works!**

---

## Summary

### Your Question: "How does it work after pushing to Git?"

**Answer:**

1. **You push code** → Git (source code storage)
2. **They clone** → Get source code from Git
3. **They install** → Build JAR with `mvn clean install`
4. **They use** → Add dependency in `pom.xml`
5. **They code** → Import and use your methods!

**Yes, they just add the dependency** — **BUT** they must run `mvn clean install` first!

### The Full Process:

```
Git Push ✅ → Clone ✅ → Install ✅ → Use ✅
```

**All 4 steps needed!**

---

## Ready to Use!

Your library is ready. Share with your team:

```bash
# Tell them to run:
git clone <your-repo>
cd common-lib
mvn clean install

# Then they can use your dependency in their projects!
```

🎉 **That's how it works!**

