package com.example.jhta_3team_finalproject.util;


public class PagingUtil  {

    public static Paging getPaging(int page, int limit, int listcount) {
        return new Paging(page, limit, listcount);
    }

    public static class Paging {
        private int page;
        private int limit;
        private int listcount;
        private int maxpage;
        private int startpage;
        private int endpage;
        private int rowNum;
        private int pagefirst;
        private int pagelast;
//        public int getTemp(int listCount, int page, int limit){
//            return listCount - (page - 1) * limit;
//        }

        public Paging(int page, int limit, int listcount) {
            this.page = page;
            this.limit = limit;
            this.listcount = listcount;
            calculatePaging();
        }

        private void calculatePaging() {

            // 총 페이지 수
            this.maxpage = (listcount + limit - 1) / limit;

            // 현재 페이지에 보여줄 시작 페이지 수
            this.startpage = ((page - 1) / 10) * 10 + 1;
            // 현재 페이지에 보여줄 마지막 페이지 수
            this.endpage = startpage + 10 - 1;

            if (endpage > maxpage) endpage = maxpage;

            this.rowNum = listcount - (page - 1) * limit;

            this.pagefirst = (page - 1) * limit + 1;//startrow
            this.pagelast = Math.min(pagefirst + limit - 1, listcount);//endrow
        }

        public int getPagefirst() {return pagefirst;}
        public int getPagelast() {return pagelast;}
        public int getPage() { return page; }
        public int getLimit() { return limit; }
        public int getListcount() { return listcount; }
        public int getMaxpage() { return maxpage; }
        public int getStartpage() { return startpage; }
        public int getEndpage() { return endpage; }
        public int getRowNum() { return rowNum; }
    }

    }