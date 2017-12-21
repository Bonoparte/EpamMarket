package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;

@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserve {
	private Integer id;
	private Integer userId;
	private Integer goodId;
	private Integer amount;
	private Timestamp reserveTime;
}
