package variable.com.eclectic.Tasks;

class TaskModel {

    private String ID, TaskTitle, TaskNotes, TaskCreateDate, TaskUpdateDate, TaskCreateTime, TaskUpdateTime;
    private String Fid, FolderTitle, FolderCreateDate, FolderUpdateDate, FolderCreateTime, FolderUpdateTime;

    public String getFid() {
        return Fid;
    }

    public void setFid(String fid) {
        Fid = fid;
    }

    public String getFolderTitle() {
        return FolderTitle;
    }

    public void setFolderTitle(String folderTitle) {
        FolderTitle = folderTitle;
    }

    public String getFolderCreateDate() {
        return FolderCreateDate;
    }

    public void setFolderCreateDate(String folderCreateDate) {
        FolderCreateDate = folderCreateDate;
    }

    public String getFolderUpdateDate() {
        return FolderUpdateDate;
    }

    public void setFolderUpdateDate(String folderUpdateDate) {
        FolderUpdateDate = folderUpdateDate;
    }

    public String getFolderCreateTime() {
        return FolderCreateTime;
    }

    public void setFolderCreateTime(String folderCreateTime) {
        FolderCreateTime = folderCreateTime;
    }

    public String getFolderUpdateTime() {
        return FolderUpdateTime;
    }

    public void setFolderUpdateTime(String folderUpdateTime) {
        FolderUpdateTime = folderUpdateTime;
    }

    public String getTaskCreateTime() {
        return TaskCreateTime;
    }

    public void setTaskCreateTime(String taskCreateTime) {
        TaskCreateTime = taskCreateTime;
    }

    public String getTaskUpdateTime() {
        return TaskUpdateTime;
    }

    public void setTaskUpdateTime(String taskUpdateTime) {
        TaskUpdateTime = taskUpdateTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTaskTitle() {
        return TaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        TaskTitle = taskTitle;
    }

    public String getTaskNotes() {
        return TaskNotes;
    }

    public void setTaskNotes(String taskNotes) {
        TaskNotes = taskNotes;
    }

    public String getTaskCreateDate() {
        return TaskCreateDate;
    }

    public void setTaskCreateDate(String taskCreateDate) {
        TaskCreateDate = taskCreateDate;
    }

    public String getTaskUpdateDate() {
        return TaskUpdateDate;
    }

    public void setTaskUpdateDate(String taskUpdateDate) {
        TaskUpdateDate = taskUpdateDate;
    }
}
