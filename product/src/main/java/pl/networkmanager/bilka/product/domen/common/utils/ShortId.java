package pl.networkmanager.bilka.product.domen.common.utils;

import java.util.UUID;

public class ShortId {
    static public String generate() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
