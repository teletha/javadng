/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser.dummy;

import java.io.Serializable;

/**
 * Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque vehicula elit id magna
 * rhoncus euismod. Maecenas fermentum lobortis dui non condimentum. Pellentesque habitant morbi
 * tristique senectus et netus et malesuada fames ac turpis egestas. In hac habitasse platea
 * dictumst. Fusce et elit in orci rhoncus tincidunt. Vestibulum venenatis at massa mattis lobortis.
 * In dapibus consequat pretium. Etiam massa lacus, dictum vitae tempor ac, iaculis nec enim.
 * Curabitur vel sem sodales, cursus lorem sed, ullamcorper velit. Morbi ac varius urna, nec
 * dignissim purus. Integer consectetur dictum venenatis.
 * <p>
 * Nam porttitor metus vel auctor pretium. Etiam gravida lorem dignissim mollis pulvinar. In augue
 * orci, feugiat in lorem eget, viverra iaculis lorem. Phasellus elit leo, accumsan nec lacus
 * aliquam, auctor porta turpis. Praesent varius dapibus augue, ut cursus neque scelerisque a.
 * Vestibulum ac bibendum tellus. Mauris interdum suscipit porta. Nam fermentum vestibulum augue,
 * quis mattis mauris placerat eget. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris
 * nec lectus at turpis efficitur pulvinar et et augue.
 * <p>
 * Nam congue sapien eu lacus hendrerit scelerisque. Curabitur sagittis, nulla sed vulputate
 * gravida, erat nisl cursus tellus, vel egestas nunc lorem ac nisl. Proin iaculis vitae risus nec
 * bibendum. Ut sodales ullamcorper convallis. Nulla in metus euismod, facilisis felis in, sagittis
 * est. Aliquam semper faucibus elit vitae posuere. Vivamus bibendum suscipit ante ut consequat.
 * Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * <p>
 * Aenean sed metus porttitor, pretium lorem in, vehicula dui. Mauris hendrerit feugiat erat, et
 * suscipit arcu placerat et. Nunc interdum consectetur congue. Sed eget fermentum leo, ut cursus
 * mauris. Etiam sagittis nulla tellus, eget maximus ante convallis molestie. Fusce vel volutpat
 * lorem, ac consectetur dolor. Duis consequat convallis diam, accumsan ultrices sapien imperdiet
 * non. Aliquam aliquam, nisl non bibendum sagittis, libero libero imperdiet elit, ac imperdiet ante
 * mi eget dui. Suspendisse a faucibus diam. Ut rhoncus urna sit amet tortor ullamcorper, a
 * tincidunt elit cursus. Sed dui tellus, eleifend ut ipsum vel, posuere interdum nisi. Morbi nec ex
 * dui. Mauris libero risus, pharetra sit amet risus nec, finibus sollicitudin leo. Pellentesque
 * hendrerit quis metus eget efficitur. Integer luctus, dolor id auctor condimentum, sapien est
 * imperdiet lectus, sed pharetra diam massa ac justo.
 * <p>
 * Sed egestas malesuada tellus et ullamcorper. Interdum et malesuada fames ac ante ipsum primis in
 * faucibus. Donec sed interdum erat. Aenean vel nulla in elit elementum eleifend ut ultricies
 * turpis. Cras sit amet auctor libero, eu tempor eros. Phasellus sit amet est nec justo dictum
 * faucibus accumsan a diam. Vivamus ultrices justo a massa vulputate condimentum.
 */
public interface InterfaceA extends InterfaceRoot, Serializable {

    /**
     * Count up characters of the specified text.
     * 
     * @param text A target text to count up characters.
     * @return A number of characters.
     */
    default int count(String text) {
        return text.length();
    }
}