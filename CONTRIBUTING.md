# Contributor's Guide for WaterMarkIt

Thank you for your interest in contributing to WaterMarkIt! This guide will walk you through the process of setting up the project, making contributions, and ensuring your code adheres to the project's formatting and style guidelines.

## Table of Contents
1. [Getting Started](#getting-started)
  - [Forking the Repository](#forking-the-repository)
  - [Cloning the Repository](#cloning-the-repository)
  - [Setting Up the Development Environment](#setting-up-the-development-environment)
2. [Making Contributions](#making-contributions)
  - [Creating a Branch](#creating-a-branch)
  - [Making Changes](#making-changes)
  - [Committing Changes](#committing-changes)
  - [Pushing Changes](#pushing-changes)
  - [Creating a Pull Request](#creating-a-pull-request)
3. [Code Formatting and Style Guidelines](#code-formatting-and-style-guidelines)
  - [General Formatting Rules](#general-formatting-rules)
  - [Java Code Style](#java-code-style)
  - [Kotlin Code Style](#kotlin-code-style)
  - [Maven and Build Configuration](#maven-and-build-configuration)
4. [Testing](#testing)
5. [Code Review Process](#code-review-process)
6. [Additional Resources](#additional-resources)

[Custom foo description](#foo)



---

## Getting Started

### Forking the Repository
1. Go to the [WaterMarkIt GitHub repository](https://github.com/OlegCheban/WaterMarkIt).
2. Click the "Fork" button in the top-right corner of the page. This will create a copy of the repository under your GitHub account.

### Cloning the Repository
1. Navigate to your forked repository on GitHub.
2. Click the "Code" button and copy the repository URL.
3. Open your terminal and run the following command to clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/WaterMarkIt.git
4. Navigate to the cloned repository:
   ```bash
   cd WaterMarkIt

### Setting Up the Development Environment
1. Ensure you have Java 11 or higher installed. You can check your Java version by running:
   ```bash
   java -version   
2. Install [Maven](https://maven.apache.org/) if you don't already have it.
3. Build the project using Maven:
   ```bash
   mvn clean install
4. Set up your IDE (e.g., IntelliJ IDEA, Eclipse) to use the project's Maven configuration.

## Making Contributions

### Creating a Branch
Before making changes, create a new branch for your work:

   ```bash
      git checkout -b feature/your-feature-name
   ```

Use a descriptive branch name that reflects the purpose of your changes.

### Making Changes
Make your changes to the codebase. Ensure you follow the Code Formatting and Style Guidelines.

Add new tests if you are introducing new functionality or modifying existing behavior.

### Committing Changes
Stage your changes:

```bash
  git add .
```

Commit your changes with a descriptive commit message:

```bash
  git commit -m "Your descriptive commit message"
```

### Pushing Changes
Push your changes to your forked repository:

```bash
  git push origin feature/your-feature-name
```

### Creating a Pull Request
1. Go to your forked repository on GitHub
2. Click the "Compare & pull request" button next to your branch
3. Fill out the pull request template with a clear description of your changes
4. Submit the pull request and wait for feedback from the maintainers

## Code Formatting and Style Guidelines
### General Formatting Rules
   - Use 4 spaces for indentation (no tabs).
   - Ensure all files end with a newline.

### Java Code Style
- Follow the Google Java Style Guide for Java code.
- Use camelCase for variable and method names.
- Use PascalCase for class names.
- Use UPPER_SNAKE_CASE for constants.
- Always include Javadoc comments for public classes, methods, and fields.
- Use @Override annotations for overridden methods.
- Don't use Java POJO, use Kotlin data classes instead.

### Kotlin Code Style
- Follow the Kotlin Coding Conventions.
- Use data class for simple data structures.

Maven and Build Configuration
Keep the pom.xml file clean and well-organized.

Use the provided Maven plugins for formatting and static analysis.

Ensure all dependencies are up-to-date and compatible with Java 11.

Testing
Write unit tests for all new functionality using JUnit 5.

Ensure all tests pass before submitting a pull request:

bash
Copy
mvn test
Use descriptive test method names that explain the purpose of the test.

Code Review Process
After submitting a pull request, the maintainers will review your changes.

Address any feedback or requested changes promptly.

Once approved, your changes will be merged into the main branch.

Additional Resources
GitHub Flow Guide

Google Java Style Guide

Kotlin Coding Conventions

Maven Documentation

Thank you for contributing to WaterMarkIt! Your efforts help make this project better for everyone. If you have any questions, feel free to reach out to the maintainers.

Happy coding! 🚀
