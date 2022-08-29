package dev.jacob_ba.timetoswim.model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dev.jacob_ba.timetoswim.Activities.AddLessonActivity;
import dev.jacob_ba.timetoswim.Activities.AddShiftActivity;

public class Controller {
    private static Controller myController;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;
    private DatabaseReference referenceTeachers;
    private DatabaseReference referenceStudents;
    private DatabaseReference referenceShifts;
    private DatabaseReference referenceRequestLessons;
    private DatabaseReference referencePrivateLessons;
    private DatabaseReference referenceGroupLessons;
    private ArrayList<Shift> shifts;
    private ArrayList<Teacher> teachers;
    private ArrayList<Student> students;
    private ArrayList<RequestLesson> requestLessons;
    private ArrayList<PrivateLesson> privateLessons;
    private ArrayList<GroupLesson> groupLessons;
    private Teacher currentTeacher;
    private Activity activity;
    private Shift currentShift;

    public static Controller getInstance() {
        if (myController == null) {
            myController = new Controller();
            myController.database = FirebaseDatabase.getInstance();
            myController.currentUser = FirebaseAuth.getInstance().getCurrentUser();
            myController.referenceTeachers = myController.database.getReference("Teachers");
            myController.referenceStudents = myController.database.getReference("Students");
            myController.referenceShifts = myController.database.getReference("Shifts");
            myController.referenceRequestLessons = myController.database.getReference("RequestLessons");
            myController.referencePrivateLessons = myController.database.getReference("PrivateLessons");
            myController.referenceGroupLessons = myController.database.getReference("GroupLessons");
        }
        return myController;
    }

    public void loadSystem(Activity activity) {
        // loadShifts() -> loadTeachers() -> getCurrentTeacher()
        this.activity = activity;
        myController.loadShifts();
    }

    public ArrayList<Shift> getTeacherShifts(String uid) {
        ArrayList<Shift> tmp = new ArrayList<>();
        for (Shift shift : shifts) {
            if (shift.getTeacherUid().equals(uid))
                tmp.add(shift);
        }
        return tmp;
    }

    public Teacher getCurrentTeacher() {
        for (Teacher t : teachers) {
            if (t.getUid().equals(currentUser.getUid())) {
                currentTeacher = t;
            }
        }
        return currentTeacher;
    }

    private void loadTeachers() {
        Log.i("Info", "Reading Teachers");
        teachers = new ArrayList<>();
        referenceTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Info", "Loading Teachers:");
                for (DataSnapshot teacherValue : snapshot.getChildren()) {
                    Teacher t = teacherValue.getValue(Teacher.class);
                    teachers.add(t);
                }
                myController.loadStudents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Teacher getTeacher(String uid) {
        for (Teacher t : teachers) {
            if (t.getUid().equals(uid))
                return t;
        }
        return null;
    }

    private void loadStudents() {
        students = new ArrayList<>();
        referenceStudents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot studentValue : snapshot.getChildren()) {
                    Student s = studentValue.getValue(Student.class);
                    students.add(s);
                }
                // Moving to the next activity
                myController.getCurrentTeacher();
                if (currentTeacher != null) {
                    Intent intent = new Intent(activity, AddShiftActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                if (currentTeacher == null) {
                    if (!isStudentExists()) {
                        Student s = new Student(currentUser.getUid(), currentUser.getDisplayName(), 0);
                        students.add(s);
                        addStudentToDatabase(s);
                    }
                    Intent intent = new Intent(activity, AddLessonActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isStudentExists() {
        for (Student s : students) {
            if (s.getUid().equals(currentUser.getUid()))
                return true;
        }
        return false;
    }

    private void loadShifts() {
        shifts = new ArrayList<>();
        referenceShifts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot shiftValue : snapshot.getChildren()) {
                    shifts.add(shiftValue.getValue(Shift.class));
                }
                myController.loadRequestLessons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Error", "Failed to read shifts from database! - " + error.toException());
            }
        });
    }

    private void loadRequestLessons() {
        requestLessons = new ArrayList<>();
        referenceRequestLessons.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Info", "=====Loading RequestLesson=====");
                for (DataSnapshot requestLessonValue : snapshot.getChildren()) {
                    Log.i("Info", "" + requestLessonValue);
                    requestLessons.add(requestLessonValue.getValue(RequestLesson.class));
                }
                myController.loadPrivateLessons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadPrivateLessons() {
        privateLessons = new ArrayList<>();
        referencePrivateLessons.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Info", "==========================================Private Lessons==========================================");
                for (DataSnapshot lessonValue : snapshot.getChildren()) {
                    Log.i("Info", "" + lessonValue);
                    privateLessons.add(lessonValue.getValue(PrivateLesson.class));
                }
                myController.loadGroupLessons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadGroupLessons() {
        groupLessons = new ArrayList<>();
        referenceGroupLessons.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Info", "==========================================Group Lessons==========================================");
                for (DataSnapshot groupLessonValue : snapshot.getChildren()) {
                    Log.i("Info", "" + groupLessonValue);
                    groupLessons.add(groupLessonValue.getValue(GroupLesson.class));
                }
                myController.loadTeachers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addTeacherToDatabase(Teacher teacher) {
        referenceTeachers.child(currentUser.getUid()).setValue(teacher);
    }

    public void addStudentToDatabase(Student student) {
        referenceStudents.child(currentUser.getUid()).setValue(student);
    }

    public void addShiftToDatabase(Shift shift) {
        shifts.add(shift);
        referenceShifts.setValue(shifts);
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public boolean isUserTeacher() {
        if (currentTeacher != null)
            return true;
        return false;
    }

    public boolean isUserStudent() {
        if (currentTeacher == null)
            return true;
        return false;
    }

    public ArrayList<Shift> getShiftsOnDateAndStyle(String date, int swimStyle) {
        ArrayList<Shift> tmp = new ArrayList<>();
        for (Shift s : shifts) {
            if (s.getStartDate().equals(date) && myController.getTeacher(s.getTeacherUid()).hasSwimStyle(swimStyle))
                tmp.add(s);
        }
        return tmp;
    }

    public void setCurrentShift(Shift shift) {
        this.currentShift = shift;
    }

    public Shift getCurrentShift() {
        return currentShift;
    }

    public Student getCurrentStudent() {
        for (Student student : students) {
            if (student.getUid().equals(currentUser.getUid()))
                return student;
        }
        return null;
    }

    public void addPrivateLessonToDatabase(PrivateLesson privateLesson) {
        privateLessons.add(privateLesson);
        referencePrivateLessons.setValue(privateLessons);
    }

    public void addGroupLessonToDatabase(GroupLesson groupLesson) {
        groupLessons.add(groupLesson);
        referenceGroupLessons.setValue(groupLessons);
    }

    public void addRequestLessonToDatabase(RequestLesson rl) {
        requestLessons.add(rl);
        referenceRequestLessons.setValue(requestLessons);
    }

    public ArrayList<Lesson> getTeacherLessons(String teacherUid) {
        ArrayList<Lesson> lessons = new ArrayList<>();
        for (Lesson lesson : groupLessons) {
            if (lesson.getTeacherUid().equals(teacherUid))
                lessons.add(lesson);
        }
        for (Lesson lesson : privateLessons) {
            if (lesson.getTeacherUid().equals(teacherUid))
                lessons.add(lesson);
        }
        return lessons;
    }

    public ArrayList<RequestLesson> getLessonRequestsOfDate(String shiftDate) {
        ArrayList<RequestLesson> requests = new ArrayList<>();
        for (RequestLesson rl : requestLessons) {
            if (rl.getStringDate().equals(shiftDate))
                requests.add(rl);
        }
        return requests;
    }

    public void removeRequestLessonFromDatabase(RequestLesson requestLesson) {
        requestLessons.remove(requestLesson);
        referenceRequestLessons.setValue(requestLessons);
    }

    public ArrayList<Lesson> getLessonsOfShift(String teacherUid, String startDate) {
        ArrayList<Lesson> shiftLessons = new ArrayList<>();
        for (PrivateLesson pl : privateLessons) {
            if (pl.getStringDate().equals(startDate) && pl.getTeacherUid().equals(teacherUid))
                shiftLessons.add(pl);
        }
        for (GroupLesson gl : groupLessons) {
            if (gl.getStringDate().equals(startDate) && gl.getTeacherUid().equals(teacherUid))
                shiftLessons.add(gl);
        }
        return shiftLessons;
    }

    public String getStudentDisplayName(String studentUid) {
        for (Student student : students) {
            if (student.getUid().equals(studentUid))
                return student.getFullName();
        }
        return null;
    }

    public void updateLessonsInDatabase() {
        referencePrivateLessons.setValue(privateLessons);
        referenceGroupLessons.setValue(groupLessons);
    }
}
