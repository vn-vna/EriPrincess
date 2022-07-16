package vn.vna.eri.v2.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UTKeyValuePair<K, V> extends UTJsonClass {

  K key;
  V value;
}
