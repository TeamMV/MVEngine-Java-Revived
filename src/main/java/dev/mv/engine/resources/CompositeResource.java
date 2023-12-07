package dev.mv.engine.resources;

import dev.mv.engine.utils.generic.pair.Pair;

public interface CompositeResource {

    Pair<Resource.Type, String>[] getDependencies();

}
