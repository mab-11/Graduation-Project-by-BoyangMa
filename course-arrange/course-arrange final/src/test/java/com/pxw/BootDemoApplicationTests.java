// package com.pxw;

// import com.github.pagehelper.PageHelper;
// import com.github.pagehelper.PageInfo;
// import com.pxw.mapper.*;
// import com.pxw.pojo.*;
// import com.pxw.service.CourseTableService;
// import jdk.nashorn.internal.ir.annotations.Ignore;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import java.util.List;


// @SpringBootTest
// class BootDemoApplicationTests {

//     @Autowired
//     private UserMapper userMapper;

//     @Autowired
//     private RoomMapper roomMapper;

//     @Autowired
//     private TeacherMapper teacherMapper;

//     @Autowired
//     private CclassesMapper cclassesMapper;

//     @Autowired
//     private CourseMapper courseMapper;

//     @Autowired
//     private CteacherMapper cteacherMapper;

//     @Autowired
//     private TaskMapper taskMapper;

//     @Autowired
//     private CourseTableMapper courseTableMapper;

//     @Autowired
//     private CourseTableService courseTableService;

//     @Test
//     void contextLoads() {
//         // PageHelper.startPage(1,2);
//         List<User> all = userMapper.findAll();

//         // PageInfo<User> userPageInfo = new PageInfo<>(all);
//         // System.out.println(userPageInfo.getTotal());
//         // System.out.println(userPageInfo.getList());
//         // System.out.println(userPageInfo.getPages());
//         // System.out.println(userPageInfo.getSize());
//         System.out.println(all);

//         User byId = userMapper.findById(1);
//         System.out.println(byId);
//     }

//     @Test
//     void testRoom(){
//         List<Room> rooms = roomMapper.selectAll();
//         System.out.println(rooms);
//     }

//     @Test
//     void testTeacher(){
//         List<Teacher> teachers = teacherMapper.selectAll();
//         System.out.println(teachers);
//     }


//     @Test
//     void testCourse(){
//         Course course = new Course();
//         List<Course> courses = courseMapper.selectByCondition(course);
//         System.out.println(courses);
//     }

//     @Test
//     void testCclasses(){
//         Cclasses cclasses = new Cclasses();
//         List<Cclasses> result = cclassesMapper.selectByCondition(cclasses);
//         System.out.println(result);
//     }

//     @Test
//     void testSubString(){
//         String str = "123,456,789,";
//         System.out.println(str.substring(0, str.length() - 1));
//     }

//     @Test
//     void testCteacher(){
//         Teacher teacher = new Teacher();
//         Course course = new Course();

//         Cteacher cteacher = new Cteacher();
//         cteacher.setCourse(course);
//         cteacher.setTeacher(teacher);
//         List<Cteacher> cteachers = cteacherMapper.selectByCondition(cteacher);
//         System.out.println(cteachers);
//     }


//     @Test
//     void testTask(){
//         // List<Task> tasks = taskMapper.selectAll();
//         // System.out.println(tasks);
//         // List<Task> tasks = taskMapper.selectByCondition();
//         // System.out.println(tasks);
//     }

//     @Test
//     void testCourseTable(){
//         List<CourseTable> courseTables = courseTableMapper.selectAll();
//         System.out.println(courseTables);
//     }


//     @Test
//     void testClassContain(){
//         String tableA = "18Computer8,18Computer9";
//         String tableB = "18Computer1,18Computer2,18Computer3,18Computer4";
//         boolean flag = false;
//         String[] names = tableA.split(",");
//         for (String classesNameA :names){
//             // Class contains means same individual class
//             if (tableB.contains(classesNameA)){
//                 flag = true;
//                 break;
//             }
//         }
//         System.out.println(flag);

//     }

//     @Test
//     void testGA(){
//         String ga = courseTableService.GA();
//         System.out.println(ga);
//     }


// }
