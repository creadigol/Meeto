package Models;

/**
 * Created by Ashfaq on 7/18/2016.
 */

public class CategoryObject extends BaseObject {

    String id, name, image;

    public CategoryObject(String s, Integer integer) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
