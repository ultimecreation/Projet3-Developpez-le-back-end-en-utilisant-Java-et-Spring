# Projet3-Developpez-le-back-end-en-utilisant-Java-et-Spring

## Setup Mysql and PhpMyAdmin

- You can follow this [tutorial](https://openclassrooms.com/fr/courses/6971126-implementez-vos-bases-de-donnees-relationnelles-avec-sql)
- You can also use [Xampp to get mysql and phpMyAdmin easily](https://www.apachefriends.org/)
- Or, if you have Docker installed on your machine, you can follow this 5 min [tutorial](https://tecadmin.net/docker-compose-for-mysql-with-phpmyadmin/)

Note: Feel free to update your environment file `application.properties`

## Setup the project itself

To make things easier, the frontend and backend code are available on this repository

- clone the project
- then navigate to the `front` folder using your integrated terminal
- run the command `npm install`

  ![front-1](https://github.com/user-attachments/assets/ba44eb81-a50e-4c1c-9359-40eb7f7b76a9)

- Once done, run the command `npm start` and the frontend will be available at [this url](http://localhost:4200/) `http://localhost:4200/`

  Now you can open a new terminal and start the backend like so :

- navigato the backend folder
- Open the Application.java file and click on the run button just above `public static void main(String[] args)`

![backend-1](https://github.com/user-attachments/assets/54b8772b-7199-447f-8d62-0e2002af0c2f)

![backend-2](https://github.com/user-attachments/assets/179d7947-aaa4-4615-9a54-115b9178d33f)

To setup the env variables :

- create a file in your at `/src/main/resources/.env`

![env](https://github.com/user-attachments/assets/7e3e684a-653b-4f3c-98c1-caa14e6e041a)

- then fill it with your credentials (see examples below)

![env](https://github.com/user-attachments/assets/09392021-4212-49dd-9cc7-40769c5e98f4)



![env-result](https://github.com/user-attachments/assets/8704e779-4fb4-4fde-9087-8d32f79b6a8c)

## Swagger

- you can find the swagger dosumentaion [here](http://localhost:8080/swagger-ui/index.html) at http://localhost:8080/swagger-ui/index.html
