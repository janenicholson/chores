package nz.net.chaosanddarkness.chores;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChoreResetConfiguration {
	private String token;
	private String dailySectionId;
	private String weeklySectionId;
	private String monthlySectionId;
	private String annualSectionId;
}
