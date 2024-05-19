import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        WelcomeMessage startMessage = new WelcomeMessage();
        startMessage.sayHi();

        Scanner scanner = new Scanner(System.in);
        Company company = new Company();

        while (true) {
            try {
                System.out.println("\nВыберите действие:");
                System.out.println("1. Добавить сотрудника");
                System.out.println("2. Уволить сотрудника");
                System.out.println("3. Прикрепить сотрудника к проекту");
                System.out.println("4. Снять сотрудника с проекта");
                System.out.println("5. Добавить новый проект");
                System.out.println("6. Закрыть проект");
                System.out.println("7. Вывести информацию о сотрудниках");
                System.out.println("8. Вывести информацию о проектах");
                System.out.println("9. Выйти из программы");

                int choice = scanner.nextInt();
                scanner.nextLine();

                // Обработка выбора пользователя
                switch (choice) {
                    case 1:
                        company.addEmployee(scanner);
                        break;
                    case 2:
                        company.dismissEmployee(scanner);
                        break;
                    case 3:
                        company.assignEmployeeToProject(scanner);
                        break;
                    case 4:
                        company.removeEmployeeFromProject(scanner);
                        break;
                    case 5:
                        company.addProject(scanner);
                        break;
                    case 6:
                        company.closeProject(scanner);
                        break;
                    case 7:
                        company.displayEmployees();
                        break;
                    case 8:
                        company.displayProjects();
                        break;
                    case 9:
                        ByeMessage stopMessage = new ByeMessage();
                        stopMessage.sayBye();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Некорректный выбор. Попробуйте снова.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введено некорректное значение. Пожалуйста, введите целое число.");
                scanner.nextLine(); // очистка буфера после ошибки
            }
        }
    }

    static class Company {
        private ArrayList<Employee> employees;
        private ArrayList<Projects> projects;

        public Company() {
            employees = new ArrayList<>();
            projects = new ArrayList<>();
        }

        public void addEmployee(Scanner scanner) {
            Employee employee = new Employee(scanner);
            employees.add(employee);
            System.out.println("Сотрудник успешно добавлен.");
        }

        public void dismissEmployee(Scanner scanner) {
            System.out.print("Введите ID сотрудника для увольнения: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            for (Employee employee : employees) {
                if (employee.getId() == id) {
                    employee.changeStatus(false);
                    System.out.println("Сотрудник уволен.");
                    return;
                }
            }
            System.out.println("Сотрудник с таким ID не найден.");
        }

        public void assignEmployeeToProject(Scanner scanner) {
            System.out.print("Введите ID сотрудника: ");
            int employeeId = scanner.nextInt();
            System.out.print("Введите ID проекта: ");
            int projectId = scanner.nextInt();
            scanner.nextLine(); // очистка буфера
            Employee employee = findEmployeeById(employeeId);
            Projects project = findProjectById(projectId);
            if (employee != null && project != null) {
                employee.addProject(projectId);
                project.addDeveloper();
                System.out.println("Сотрудник прикреплен к проекту.");
            } else {
                System.out.println("Сотрудник или проект не найден.");
            }
        }

        public void removeEmployeeFromProject(Scanner scanner) {
            System.out.print("Введите ID сотрудника: ");
            int employeeId = scanner.nextInt();
            System.out.print("Введите ID проекта: ");
            int projectId = scanner.nextInt();
            scanner.nextLine(); // очистка буфера
            Employee employee = findEmployeeById(employeeId);
            Projects project = findProjectById(projectId);
            if (employee != null && project != null) {
                employee.removeProject(projectId);
                project.removeDeveloper();
                System.out.println("Сотрудник снят с проекта.");
            } else {
                System.out.println("Сотрудник или проект не найден.");
            }
        }

        public void addProject(Scanner scanner) {
            Projects project = new Projects(scanner);
            projects.add(project);
            System.out.println("Проект успешно добавлен.");
        }

        public void closeProject(Scanner scanner) {
            System.out.print("Введите ID проекта для закрытия: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // очистка буфера
            for (Projects project : projects) {
                if (project.getId() == id) {
                    project.finishProject();
                    System.out.println("Проект закрыт.");
                    return;
                }
            }
            System.out.println("Проект с таким ID не найден.");
        }

        public void displayEmployees() {
            if (employees.isEmpty()) {
                System.out.println("Сотрудники отсутствуют.");
            } else {
                for (Employee employee : employees) {
                    employee.displayEmployee();
                }
            }
        }

        public void displayProjects() {
            if (projects.isEmpty()) {
                System.out.println("Проекты отсутствуют.");
            } else {
                for (Projects project : projects) {
                    project.displayProject();
                }
            }
        }

        private Employee findEmployeeById(int id) {
            for (Employee employee : employees) {
                if (employee.getId() == id) {
                    return employee;
                }
            }
            return null;
        }

        private Projects findProjectById(int id) {
            for (Projects project : projects) {
                if (project.getId() == id) {
                    return project;
                }
            }
            return null;
        }
    }

    static class Employee {
        private static int idCounter = 0;
        private String name;
        private boolean status;
        private String position;
        private int id;
        private boolean flag;
        private ArrayList<Integer> listOfProjects;
        private final int MIN_LENGTH_OF_EMPLOYEE_NAME = 3;
        private final int MAX_LENGTH_OF_EMPLOYEE_NAME = 15;
        private final String indentation = "                     ";
        private Scanner input;

        public Employee(Scanner scanner) {
            this.input = scanner;
            this.status = true;
            this.flag = false;
            this.listOfProjects = new ArrayList<>();

            System.out.println("\n" + indentation + "            Сотрудник");
            System.out.println(indentation + "------------------------------");
            boolean resultOfChecking;
            String checking;
            do {
                System.out.print(indentation + "      Имя | ");
                checking = input.nextLine();
                resultOfChecking = checkLength(checking);
            } while(resultOfChecking);

            do {
                System.out.print(indentation + "Должность | ");
                checking = input.nextLine();
                resultOfChecking = testOfCorrectness(checking);
            } while (resultOfChecking);

            System.out.println(indentation + "------------------------------\n");
            this.id = ++idCounter;
        }

        private boolean testOfCorrectness(String checking) {
            char n = checking.charAt(0);

            switch (n) {
                case 'm':
                case 'M':
                    this.position = "Менеджер";
                    return false;
                case 'd':
                case 'D':
                    this.position = "Разработчик";
                    return false;
                case 'r':
                case 'R':
                    this.position = "Ресурсный менеджер";
                    return false;
                default:
                    System.out.println("\n      Пожалуйста, введите корректную должность! \n");
                    return true;
            }
        }

        private boolean checkLength(String line) {
            if (line.length() >= MIN_LENGTH_OF_EMPLOYEE_NAME && line.length() < MAX_LENGTH_OF_EMPLOYEE_NAME) {
                this.name = line;
                return false;
            } else {
                System.out.println("\n      Длина имени должна быть не менее 3 и не более 15 символов!\n");
                return true;
            }
        }

        public void addProject(int projectId) {
            listOfProjects.add(projectId);
        }

        public void removeProject(int projectId) {
            listOfProjects.remove(Integer.valueOf(projectId));
        }

        public void changeStatus(boolean newStatus) {
            this.status = newStatus;
        }

        public int getId() {
            return id;
        }

        public void displayEmployee() {
            System.out.println("\n" + indentation + "            Сотрудник");
            System.out.println(indentation + "------------------------------");
            System.out.println(indentation + "         ID| " + id);
            System.out.println(indentation + "        Имя| " + name);
            System.out.println(indentation + "  Должность| " + position);
            System.out.println(indentation + "     Статус| " + (status ? "Работает" : "Уволен"));
            System.out.println(indentation + "------------------------------\n");
        }
    }

    static class Projects {
        private static int idCounter = 0;
        private String name;
        private String description;
        private boolean status;
        private int manager;
        private int resourceManager;
        private int id;
        private boolean flag;
        private final int MIN_LENGTH_OF_PROJECT_NAME = 5;
        private final int MAX_LENGTH_OF_PROJECT_NAME = 20;
        private final int MIN_LENGTH_OF_PROJECT_DESCRIPTION = 15;
        private final int MAX_LENGTH_OF_PROJECT_DESCRIPTION = 50;
        private final String indentation = "                ";
        private Scanner input;

        public Projects(Scanner scanner) {
            this.input = scanner;
            this.status = true;
            this.flag = false;

            System.out.println("\n" + indentation + "         Проект");
            System.out.println(indentation + "-----------------------------------------");

            boolean resultOfChecking;
            String checking;
            do {
                System.out.print(indentation + "       Название | ");
                checking = input.nextLine();
                resultOfChecking = checkLength(checking, "Name");
            } while (resultOfChecking);

            do {
                System.out.print(indentation + "       Описание | ");
                checking = input.nextLine();
                resultOfChecking = checkLength(checking, "Description");
            } while (resultOfChecking);

            System.out.print(indentation + "           Менеджер | ");
            this.manager = input.nextInt();
            System.out.print(indentation + " Ресурсный менеджер | ");
            this.resourceManager = input.nextInt();
            input.nextLine(); // очистка буфера
            System.out.println(indentation + "-----------------------------------------\n");

            this.id = ++idCounter;
        }

        private boolean checkLength(String line, String type) {
            switch (type) {
                case "Name":
                    if (line.length() >= MIN_LENGTH_OF_PROJECT_NAME && line.length() <= MAX_LENGTH_OF_PROJECT_NAME) {
                        this.name = line;
                        return false;
                    } else {
                        System.out.println("\n      Длина названия должна быть не менее 5 и не более 20 символов!\n");
                        return true;
                    }
                case "Description":
                    if (line.length() >= MIN_LENGTH_OF_PROJECT_DESCRIPTION && line.length() <= MAX_LENGTH_OF_PROJECT_DESCRIPTION) {
                        this.description = line;
                        return false;
                    } else {
                        System.out.println("\n      Длина описания должна быть не менее 15 и не более 50 символов!\n");
                        return true;
                    }
                default:
                    return true;
            }
        }

        public void finishProject() {
            this.status = false;
        }

        public void addDeveloper() {
            this.flag = true;
        }

        public void removeDeveloper() {
            this.flag = false;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getManager() {
            return manager;
        }

        public int getResourceManager() {
            return resourceManager;
        }

        public boolean getStatus() {
            return status;
        }

        public boolean getFlag() {
            return flag;
        }

        public void displayProject() {
            System.out.println("\n" + indentation + "            Информация о проекте");
            System.out.println(indentation + "-----------------------------------------");
            System.out.println(indentation + "                  ID | " + id);
            System.out.println(indentation + "            Название | " + name);
            System.out.println(indentation + "            Описание | " + description);
            System.out.println(indentation + "            Менеджер | " + manager);
            System.out.println(indentation + "  Ресурсный менеджер | " + resourceManager);
            System.out.println(indentation + "        Разработчики | " + (flag ? "есть" : "нет"));
            System.out.println(indentation + "              Статус | " + (status ? "В процессе" : "Завершен"));
            System.out.println(indentation + "-----------------------------------------\n");
        }
    }

    static class WelcomeMessage {
        private String[] welcomeArt = {
                " ╭╮╭╮╭┳━━━┳╮╱╱╭━━━┳━━━┳━╮╭━┳━━━╮",
                " ┃┃┃┃┃┃╭━━┫┃╱╱┃╭━╮┃╭━╮┃┃╰╯┃┃╭━━╯",
                " ┃┃┃┃┃┃╰━━┫┃╱╱┃┃╱╰┫┃╱┃┃╭╮╭╮┃╰━━╮",
                " ┃╰╯╰╯┃╭━━┫┃╱╭┫┃╱╭┫┃╱┃┃┃┃┃┃┃╭━━╯",
                " ╰╮╭╮╭┫╰━━┫╰━╯┃╰━╯┃╰━╯┃┃┃┃┃┃╰━━╮",
                "  ╱╰╯╰╯╰━━━┻━━━┻━━━┻━━━┻╯╰╯╰┻━━━╯"
        };

        public void sayHi() {
            System.out.println("\n\n");
            for (String line : welcomeArt) {
                System.out.println(line);
            }
        }
    }

    static class ByeMessage {
        private String[] byeArt = {
                        "⠀⠀⠀⠀⠀⢀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀",
                        "⠀⠀⠀⠀⢰⣿⡿⠗⠀⠠⠄⡀⠀⠀⠀⠀",
                        "⠀⠀⠀⠀⡜⠁⠀⠀⠀⠀⠀⠈⠑⢶⣶⡄",
                        "⢀⣶⣦⣸⠀⢼⣟⡇⠀⠀⢀⣀⠀⠘⡿⠃",
                        "⠀⢿⣿⣿⣄⠒⠀⠠⢶⡂⢫⣿⢇⢀⠃⠀",
                        "⠀⠈⠻⣿⣿⣿⣶⣤⣀⣀⣀⣂⡠⠊⠀⠀",
                        "⠀⠀⠀⠃⠀⠀⠉⠙⠛⠿⣿⣿⣧⠀⠀⠀",
                        "⠀⠀⠘⡀⠀⠀⠀⠀⠀⠀⠘⣿⣿⡇⠀⠀",
                        "⠀⠀⠀⣷⣄⡀⠀⠀⠀⢀⣴⡟⠿⠃⠀⠀",
                        "⠀⠀⠀⢻⣿⣿⠉⠉⢹⣿⣿⠁⠀⠀⠀⠀",
                        "⠀⠀⠀⠀⠉⠁⠀⠀⠀⠉⠁⠀⠀⠀⠀⠀"
        };

        public void sayBye() {
            for (String line : byeArt) {
                System.out.println(line);
            }
        }
    }
}
