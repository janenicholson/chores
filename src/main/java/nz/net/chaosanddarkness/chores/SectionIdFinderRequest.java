package nz.net.chaosanddarkness.chores;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionIdFinderRequest {
	private String token;
	private String workspaceName;
	private String projectName;
	private String sectionName;
}
