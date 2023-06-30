package com.mss.chatgptmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ttpfx
 * @date 2023/3/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
   private String finish_reason;
   private Integer index;
   private Delta delta;
}
