package kimjiwon.sigongeducation.recyclerviewex2.model;

/**
 * Created by kimjiwon on 2018. 1. 2..
 */

public class LectureModel {
    public int lectureNum;
    public String lectureTitle;
    public int lectureTime;
    public int lecturePage;

    public LectureModel(int lectureNum, String lectureTitle, int lectureTime, int lecturePage) {
        this.lectureNum = lectureNum;
        this.lectureTitle = lectureTitle;
        this.lectureTime = lectureTime;
        this.lecturePage = lecturePage;
    }
}
