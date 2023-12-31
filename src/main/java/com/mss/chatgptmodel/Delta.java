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
public class Delta {
   private String content;
   private String role;
}
