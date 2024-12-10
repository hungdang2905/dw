package warehouse.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Province_Dim {

        private int id;
        private String name;
        private String region;

        public Province_Dim(String name, String region) {
                this.name = name;
                this.region = region;
        }
        public Province_Dim(){}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
        
}
