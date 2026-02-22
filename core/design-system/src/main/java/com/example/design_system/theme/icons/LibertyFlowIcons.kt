package com.example.design_system.theme.icons

import com.example.design_system.R

import androidx.annotation.DrawableRes

/**
 * Central repository for all drawable resource identifiers used within the LibertyFlow UI.
 * * Icons are categorized by their visual style and behavior to ensure consistency
 * across the application.
 */
object LibertyFlowIcons {

    /**
     * Icons that support property or vector animation transitions.
     */
    object Animated {
        @DrawableRes val Home = R.drawable.ic_home_animated
        @DrawableRes val Heart = R.drawable.ic_heart_animated
        @DrawableRes val Book = R.drawable.ic_book_animated
        @DrawableRes val Dots = R.drawable.ic_dots_animated
        @DrawableRes val Eye = R.drawable.ic_eye_animated
        @DrawableRes val PlayPause = R.drawable.ic_play_pause_animated
        @DrawableRes val Crop = R.drawable.ic_crop_animated
        @DrawableRes val ArrowUp = R.drawable.ic_arrow_up_animated
        @DrawableRes val Clock = R.drawable.ic_clock_animated
    }

    /**
     * Standard monochromatic outlined icons typically used for navigation and actions.
     */
    object Outlined {
        @DrawableRes val AlarmSleep = R.drawable.ic_alarm_sleep
        @DrawableRes val Magnifier = R.drawable.ic_magnifier
        @DrawableRes val CrossCircle = R.drawable.ic_cross_circle
        @DrawableRes val User = R.drawable.ic_user
        @DrawableRes val Password = R.drawable.ic_password
        @DrawableRes val FunnyCube = R.drawable.ic_funny_cube
        @DrawableRes val Filters = R.drawable.ic_filters
        @DrawableRes val Cat = R.drawable.ic_cat
        @DrawableRes val PlusCircle = R.drawable.ic_plus_circle
        @DrawableRes val MinusCircle = R.drawable.ic_minus_circle
        @DrawableRes val DangerCircle = R.drawable.ic_danger_circle
        @DrawableRes val Logout = R.drawable.ic_logout
        @DrawableRes val Info = R.drawable.ic_info
        @DrawableRes val Settings = R.drawable.ic_settings
        @DrawableRes val ListArrowDown = R.drawable.ic_list_arrow_down
        @DrawableRes val Rewind = R.drawable.ic_rewind
        @DrawableRes val RewindBack = R.drawable.ic_rewind_back
        @DrawableRes val FullScreen = R.drawable.ic_full_screen
        @DrawableRes val Next = R.drawable.ic_next
        @DrawableRes val Previous = R.drawable.ic_previous
        @DrawableRes val Pip = R.drawable.ic_pip
        @DrawableRes val Lock = R.drawable.ic_lock
        @DrawableRes val Unlock = R.drawable.ic_unlock
        @DrawableRes val Checklist = R.drawable.ic_checklist
        @DrawableRes val ArrowDown = R.drawable.ic_arrow_down
        @DrawableRes val ArrowRightCircle = R.drawable.ic_arrow_right_circle
        @DrawableRes val HighQuality = R.drawable.ic_high_quality
        @DrawableRes val RewindForwardCircle = R.drawable.ic_rewind_forward_circle
        @DrawableRes val PlayCircle = R.drawable.ic_play_circle
        @DrawableRes val CheckCircle = R.drawable.ic_check_circle
        @DrawableRes val Rocket = R.drawable.ic_rocket
        @DrawableRes val GitHub = R.drawable.ic_github
        @DrawableRes val Layers = R.drawable.ic_layers
        @DrawableRes val PieChart = R.drawable.ic_pie_chart
        @DrawableRes val Colour = R.drawable.ic_colour
        @DrawableRes val Tablet = R.drawable.ic_tablet
        @DrawableRes val Crop = R.drawable.ic_crop
    }

    /**
     * Icons with solid fills, often used for active states or emphasized actions.
     */
    object Filled {
        @DrawableRes val ArrowLeft = R.drawable.ic_arrow_left_filled
        @DrawableRes val DoubleArrowUp = R.drawable.ic_double_arrow_up_filled
        @DrawableRes val DoubleArrowDown = R.drawable.ic_double_arrow_down_filled
        @DrawableRes val Download = R.drawable.ic_download_filled
        @DrawableRes val DoubleCheck = R.drawable.ic_double_check_filled
        @DrawableRes val Play = R.drawable.ic_play_filled
        @DrawableRes val AltArrowLeft = R.drawable.ic_alt_arrow_left_filled
        @DrawableRes val AltArrowRight = R.drawable.ic_alt_arrow_right_filled
        @DrawableRes val ListCheck = R.drawable.ic_list_check_filled
    }

    /**
     * Brand logos and icons that contain specific, multi-tone color palettes.
     */
    object Multicolored {
        @DrawableRes val AniLiberty = R.drawable.ic_aniliberty_multicolored
        @DrawableRes val LibertyFlow = R.drawable.ic_liberty_flow_multicolored
        @DrawableRes val VK = R.drawable.ic_vk_multicolored
        @DrawableRes val YouTube = R.drawable.ic_youtube_multicolored
        @DrawableRes val Patreon = R.drawable.ic_patreon_multicolored
        @DrawableRes val Telegram = R.drawable.ic_telegram_multicolored
        @DrawableRes val Discord = R.drawable.ic_discord_multicolored
    }

    /**
     * High-resolution illustrations, typically used for onboarding or empty states.
     */
    object Illustrations {
        @DrawableRes val Welcome = R.drawable.welcome
        @DrawableRes val Vpn = R.drawable.vpn
        @DrawableRes val Permissions = R.drawable.permissions
    }
}