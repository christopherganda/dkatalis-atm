# ATM

## Problem Statement
The program is in CLI to simulate an ATM with a retail bank.

The submission must include an executable `start.sh` file located at the root. The executable is executed in an environment fully configured for Java (with the `java` executable in the `PATH` and `JAVA_HOME` environment variable pointing to the `JDK`). All SDKs are set to their latest versions. The environment also has internet access, allowing package management tools to download additional dependencies as needed (e.g., `./gradlew build`, `./mvnw package`).

## Functional Requirement
Notes: We are not allowed to use any third-party frameworks, so we will optimize by algorithm and data structure only.

### 1. Login
  Logs in as the user and creates new user if not exist.<br />
  Command: `login [name]`<br />
#### Key Consideration
  1. Read: We will fetch record based on the name and it has to be fast as the system grows.
  2. Write: The write process has to be fast and handles race condition.
  3. Resizing: If we only utilize data structure, that means the DS has to be optimal to resize.
### 2. Deposit
  Logged in user can deposit any amounts.<br />
  Command: `deposit [amount]`<br />
#### Key Consideration

### 3. Withdraw
  Logged in user can withdraw maximum of user's balance.<br />
  Command: `withdraw [amount]`<br />
#### Key Consideration

### 4. Transfer
  Logged in user can transfer to other valid user with maximum of the user's balance.<br />
  Command: `transfer [name] [amount]`<br />
#### Key Consideration

### 5. Logout
  Logs out current logged in user.<br />
  Command: `logout`<br />
#### Key Consideration

## Non-Functional Requirement
1. Auto tests are required. The program will be given valid and invalid inputs, so we have to make sure to handle edge cases and errors gracefully.
