package com.acv.chat.data.openai

import kotlinx.serialization.Serializable

@Serializable(with = FunctionModeSerializer::class)
public sealed interface FunctionMode {

    /**
     * Represents a function call mode.
     * The value can be any string representing a specific function call mode.
     */
    @JvmInline
    public value class Default(public val value: String) : FunctionMode

    /**
     * Represents a named function call mode.
     * The name indicates a specific function that the model will call.
     *
     * @property name the name of the function to call.
     */
    @Serializable
    public data class Named(public val name: String) : FunctionMode

    /** Provides default function call modes. */
    public companion object {
        /** Represents the `auto` mode. */
        public val Auto: FunctionMode = Default("auto")

        /** Represents the `none` mode. */
        public val None: FunctionMode = Default("none")
    }
}
