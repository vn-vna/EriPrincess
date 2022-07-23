package vn.vna.eri.v2.configs.helper;

import lombok.Getter;

@Getter
public enum LangPackEnum {
  VI_VN("vi-vn"),
  EN_US("en-us");

  final String name;

  LangPackEnum(String name) {
    this.name = name;
  }
}
