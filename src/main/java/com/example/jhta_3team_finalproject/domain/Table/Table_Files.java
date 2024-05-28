package com.example.jhta_3team_finalproject.domain.Table;

public class Table_Files {

    private int board_num;
    private String original_file_name;
    private String file_name;
    private String upload_date;

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getOriginal_file_name() {
        return original_file_name;
    }

    public void setOriginal_file_name(String original_file_name) {
        this.original_file_name = original_file_name;
    }

    public int getBoard_num() {
        return board_num;
    }

    public void setBoard_num(int board_num) {
        this.board_num = board_num;
    }

    @Override
    public String toString() {
        return "Table_Files{" +
                "board_num=" + board_num +
                ", original_file_name='" + original_file_name + '\'' +
                ", file_name='" + file_name + '\'' +
                ", upload_date='" + upload_date + '\'' +
                '}';
    }
}
