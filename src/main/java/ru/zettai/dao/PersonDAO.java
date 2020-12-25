package ru.zettai.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.zettai.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
        Создание подключения к базе данных через стандартные средства JDBC
        private static Connection connection; // класс для соединения с БД
        static {
            //загружаем наш драйвер для postgres и убеждаемся, что он присутствует в оперативке
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            // используем DriverManager, чтобы создать соединение с необходимыми параметрами
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        //Чтение всего списка людей из БД стандартными средствами JDBC
        public List<Person> index() {
            List<Person> people = new ArrayList<>();
            // получаем statement - объект с SQL запросом к базе данных
            // можно не заменять на PreparedStatement, так как здесь статический SQL запрос без ввода от пользователя
            try {
                Statement statement = connection.createStatement();
                String SQL_SELECT = "SELECT * FROM Person"; //sql запрос на получение всех строк, имеющихся в таблице person
                ResultSet resultSet = statement.executeQuery(SQL_SELECT); //получаем ResultSet со всеми результатами SQL запроса (не изменяем данные в БД)
                //цикл обработки сета результатов
                while (resultSet.next()){
                    Person person = new Person();

                    person.setId(resultSet.getInt("id"));
                    person.setName(resultSet.getString("name"));
                    person.setEmail(resultSet.getString("email"));
                    person.setAge(resultSet.getInt("age"));

                    people.add(person);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return people;
        }


        //Возращает конкретного человека из БД по указанному индексу стандартными средствами JDBC
         public Person show(int id) {
            Person person = null;
            try(PreparedStatement preparedStatement =
                        connection.prepareStatement("select * from Person where id=?")){

                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                //получаем следующую строку из resultset с данными человека
                resultSet.next();
                //добавляем в новый экземпляр Person поля, полученные по запросу select
                person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return person;
        }

        //Добавляет нового человека в БД стандартными средствами JDBC
        public void save(Person person) {
            //Используем PreparedStatement вместо обычного Statement
            try (PreparedStatement preparedStatement= connection.prepareStatement("insert into Person values (1, ?, ?, ?)")){
                preparedStatement.setString(1, person.getName());
                preparedStatement.setInt(2, person.getAge());
                preparedStatement.setString(3, person.getEmail());

                preparedStatement.executeUpdate(); // добавляем значения, указанные в параметрах VALUES в БД
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



        //обновляет инфу по человеку стандартными средствами JDBC
        public void update(int id, Person updatePerson) {
            try(PreparedStatement preparedStatement =
                        connection.prepareStatement("update Person set name=?, age=?, email=? where id=?")){

                preparedStatement.setString(1,updatePerson.getName());
                preparedStatement.setInt(2, updatePerson.getAge());
                preparedStatement.setString(3,updatePerson.getEmail());
                preparedStatement.setInt(4, id);

                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        //удаляем человека из БД по его id стандартными средствами JDBC
        public void delete(int id) {
            try(PreparedStatement preparedStatement = connection.prepareStatement("delete from Person where  id=?")){
                preparedStatement.setInt(1, id);

                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    */

    // Чтение всего списка людей из базы данных при помощи JdbcTemplate
    public List<Person> index(){
        return jdbcTemplate.query("SELECT * FROM Person", new PersonMapper());
    }

    //Возращает конкретного человека из БД по указанному индексу при помощи JdbcTemplate
    //В данном случае вместо нашего RowMapper используем стандартную реализацию Springa BeanPropertyRowMapper,
    //которая подхватывает названия полей передаваемого в параметрах класса и назначает им значения из БД
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    //Добавляет нового человека в БД при помощи JdbcTemplate
    public void save(Person person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Person(NAME, AGE, EMAIL) VALUES(?, ?, ?)", new String[]{"id"});
            ps.setString(1, person.getName());
            ps.setInt(2, person.getAge());
            ps.setString(3, person.getEmail());
            return ps;
        }, keyHolder);
//        jdbcTemplate.update("INSERT INTO Person VALUES(?, ?, ?, ?)", keyHolder.getKey(), person.getName(), person.getAge(), person.getEmail());
    }


    //обновляет инфу по человеку при помощи JdbcTemplate
    public void update(int id, Person updatePerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id=?", updatePerson.getName(), updatePerson.getAge(), updatePerson.getEmail(), id);
    }

    //удаляем человека из БД по его id при помощи JdbcTemplate
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }
}
