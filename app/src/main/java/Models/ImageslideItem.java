package Models;

/**
 * Created by Creadigol on 07-09-2016.
 */
public class ImageslideItem {

        // Getter and Setter model for recycler view items
        private String title;
        private int image;

        public ImageslideItem(String title,  int image) {

            this.title = title;

            this.image = image;
        }

        public String getTitle() {
            return title;
        }



        public int getImage() {
            return image;
        }
}
