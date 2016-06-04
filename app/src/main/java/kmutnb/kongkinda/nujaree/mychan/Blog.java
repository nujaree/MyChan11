package kmutnb.kongkinda.nujaree.mychan;

/**
 * Created by vutchai14 on 7/1/2559.
 */

import java.util.List;

public class Blog {
    String status;

    int count;

    int totalCount;

    String Source;

    int pages;

    List<Post> Chan;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String Source) {
        this.Source = Source;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<Post> getPosts() {
        return Chan;
    }

    public void setPosts(List<Post> Chan) {
        this.Chan = Chan;
    }
}
