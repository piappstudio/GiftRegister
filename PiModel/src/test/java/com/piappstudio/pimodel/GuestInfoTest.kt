package com.piappstudio.pimodel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GuestInfoTest {

    @Test
    fun `validate guest info displayType`() {
        // Acitivyt, Fragment, Context.
        var guest = GuestInfo(giftType = GiftType.CASH, giftValue = "100")
        assertEquals("$100.00", guest.displayGiftValue())
        guest = GuestInfo(giftType = GiftType.CASH, giftValue = "2 pound")
        assertEquals(null, guest.displayGiftValue())
    }
    @Test
    fun `validate guest info displayType when gift type is gold`() {
        var guest = GuestInfo(giftType = GiftType.GOLD, giftValue = "100")
        assertEquals("100", guest.displayGiftValue())
        guest = GuestInfo(giftType = GiftType.GOLD, giftValue = "1 pound")
        assertEquals("1 pound", guest.displayGiftValue())
    }


}