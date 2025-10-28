# How The Library Works - Complete Workflow

Let me explain exactly how this works step by step with real examples.

## 📚 Scenario: You've Pushed to Git

You have your library at: `https://github.com/your-org/common-lib` (private repo)

---

## Step-by-Step: How Others Use Your Library

### Step 1: You Push to Git ✅

```bash
# You did this:
git push origin main
```

**Your library code is now in Git repository**

---

### Step 2: Your Team Member Wants to Use It

**They need to clone and install it first:**

```bash
# They run this on their computer:
git clone git@github.com:your-org/common-lib.git
cd common-lib
mvn clean install
```

**What happens:**
- Code is cloned to their computer
- Library is built into JAR file
- JAR is installed to their local Maven repository at: `~/.m2/repository/com/lyhorng/common-lib/`

**Now they can use it in their projects!**

---

### Step 3: They Use It in Their Project

**In their project's `pom.xml`, they add:**

```xml
<dependencies>
    <!-- Your library -->
    <dependency>
        <groupId>com.lyhorng</groupId>
        <artifactId>common-lib</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

---

### Step 4: They Use the Library Code

**In their Java files:**

```java
import com.lyhorng.common.util.StringUtils;
import com.lyhorng.common.response.ApiResponse;

@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        
        // ✅ Using YOUR library method
        return ApiResponse.success(user);
    }
    
    public void validateEmail(String email) {
        // ✅ Using YOUR library method
        if (StringUtils.isValidEmail(email)) {
            // Email is valid!
        }
    }
}
```

---

### Step 5: When You Add New Features

**You add a new method:**

```java
// You added this to your library
public class StringUtils {
    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }
}
```

**You deploy:**
```bash
# 1. Update version in pom.xml
<version>1.1.0</version>

# 2. Build
mvn clean install

# 3. Commit and push
git add .
git commit -m "feat: add reverse string method"
git push origin main
```

**Your team member updates:**
```bash
cd common-lib
git pull origin main        # Get your new code
mvn clean install            # Build the new version

# They can now use your new method!
String reversed = StringUtils.reverse("Hello");
```

---

## 🎯 Visual Workflow

```
┌─────────────────────────────────────────────────────────┐
│  STEP 1: YOU (Library Owner)                            │
│  ┌────────────────────────────────────────────────┐    │
│  │ common-lib/                                      │    │
│  │   ├── src/main/java/com/lyhorng/common/        │    │
│  │   │   ├── util/StringUtils.java  ✅           │    │
│  │   │   ├── util/DateUtils.java    ✅           │    │
│  │   │   └── response/ApiResponse.java ✅        │    │
│  └────────────────────────────────────────────────┘    │
│                              ↓                           │
│                       git push origin main               │
└─────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────┐
│  STEP 2: GIT REPOSITORY (Private)                      │
│  ┌────────────────────────────────────────────────┐    │
│  │ github.com/your-org/common-lib 🔒              │    │
│  │   All your code is stored here                │    │
│  └────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────┐
│  STEP 3: TEAM MEMBER (User)                            │
│  ┌────────────────────────────────────────────────┐    │
│  │ git clone git@github.com:your-org/common-lib  │    │
│  │ cd common-lib                                   │    │
│  │ mvn clean install  ← Builds JAR file          │    │
│  └────────────────────────────────────────────────┘    │
│                              ↓                           │
│  ┌────────────────────────────────────────────────┐    │
│  │ ~/.m2/repository/com/lyhorng/common-lib/       │    │
│  │   ├── common-lib-1.0.0.jar ✅                │    │
│  │   └── common-lib-1.0.0-sources.jar ✅        │    │
│  └────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────┐
│  STEP 4: THEIR PROJECT                                  │
│  ┌────────────────────────────────────────────────┐    │
│  │ pom.xml                                        │    │
│  │   <dependency>                                 │    │
│  │     <groupId>com.lyhorng</groupId>            │    │
│  │     <artifactId>common-lib</artifactId>       │    │
│  │     <version>1.0.0</version>                 │    │
│  │   </dependency>                               │    │
│  └────────────────────────────────────────────────┘    │
│                              ↓                           │
│  ┌────────────────────────────────────────────────┐    │
│  │ TheirProject/src/main/java/                   │    │
│  │   import com.lyhorng.common.util.StringUtils; │    │
│  │   import com.lyhorng.common.response.*;      │    │
│  │   // ✅ They can use your methods now!       │    │
│  └────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

---

## 💡 Key Points Explained

### 1. Git Repository is NOT Maven Repository

**Git = Version control (stores your code)**  
**Maven Repository = Build artifacts (JAR files)**

Your code is in Git, but to use it, it must be "installed" to a Maven repository (local or remote).

### 2. Two Ways to Share Library

**Option A: Each person installs locally** (Free, Simple)
```bash
# Everyone does this once
git clone <repo>
cd common-lib
mvn clean install
```

**Option B: Use shared Maven repository** (Professional, Better)
```bash
# You deploy once
mvn clean deploy

# They use directly (no clone needed!)
# Just add dependency to pom.xml
```

### 3. What `mvn clean install` Does

```bash
mvn clean install
```

**This command:**
1. Compiles your Java code
2. Creates JAR file
3. Installs to `~/.m2/repository/com/lyhorng/common-lib/`
4. Makes it available to other projects on that computer

---

## 🔄 Complete Workflow Example

### You (Adding Feature):

```bash
# 1. Create new feature
cd common-lib
git checkout -b feature/add-email-validator

# 2. Add code to StringUtils.java
# ... add your code ...

# 3. Build
mvn clean install

# 4. Commit and push
git add .
git commit -m "feat: add email validator"
git push origin feature/add-email-validator

# 5. Merge to main
git checkout main
git merge feature/add-email-validator
git push origin main
```

### Your Team Member (Using Your Feature):

```bash
# 1. Get your new code
cd common-lib
git pull origin main

# 2. Build the new version
mvn clean install

# 3. Use in their project
```

```java
// Now they can use your new method
import com.lyhorng.common.util.StringUtils;

if (StringUtils.isValidEmail("test@example.com")) {
    // ✅ Your new method works!
}
```

---

## ❓ Common Questions

### Q: After I push to Git, is my library ready to use?

**A: Not yet!** Your team needs to:
1. Clone the repository
2. Run `mvn clean install`
3. Then they can use it

**Git stores the code, but they need to build it first!**

### Q: Why not just clone and use?

**A: Because Maven needs JAR files, not source code!**

- ✅ Git has: Your `.java` files
- ❌ Maven needs: `.jar` files
- Solution: `mvn clean install` converts `.java` → `.jar`

### Q: Do I need to setup a Maven repository server?

**A: No!** You can use local install method (Option A above).  
**Yes, if:** You want centralized deployment (Option B, more professional).

### Q: What if I deploy to Maven repository?

**Then it's easier!**

**You:**
```bash
mvn clean deploy  # Deploy to server
```

**They:**
```bash
# No clone needed! Just use it
<dependency>...</dependency>
```

---

## 📋 Quick Checklist

### For You (Library Owner):
- [ ] Push code to Git ✅
- [ ] Build with `mvn clean install`
- [ ] Tell team to install it
- [ ] When adding features, update version in pom.xml
- [ ] Tag releases: `git tag v1.0.1`

### For Your Team Members:
- [ ] Clone repository (need Git access)
- [ ] Run `mvn clean install`
- [ ] Add dependency to their `pom.xml`
- [ ] Use your methods in their code
- [ ] When you update, they run: `git pull && mvn clean install`

---

## 🎯 Summary

### Your Question: "Anyone can use it just by adding dependency, right?"

**Short Answer:** After installing it locally, YES!

**Complete Process:**

```
You Push to Git
    ↓
Team Clone + Install (`mvn clean install`)
    ↓
Team Add Dependency to pom.xml
    ↓
Team Use Your Methods ✅
```

### The Key:
- Git = Source code storage
- `mvn install` = Build JAR file
- Maven dependency = Use the JAR file

**Both steps needed!** (Clone + Install)

---

## 🚀 Better Alternative (Optional)

Instead of everyone installing locally, setup Maven Repository:

**You:**
```bash
mvn clean deploy  # Deploy to server
```

**Team:**
```xml
<!-- Just add this, no clone needed! -->
<dependency>
    <groupId>com.lyhorng</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

See [DEPLOYMENT.md](DEPLOYMENT.md) for setup instructions.

---

## ✅ You're All Set!

Your library is ready. Now:
1. ✅ Team clones it
2. ✅ Team runs `mvn clean install`
3. ✅ Team uses your methods

**That's it!** 🎉

