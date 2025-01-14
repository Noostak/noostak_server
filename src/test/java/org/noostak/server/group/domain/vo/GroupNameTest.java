package org.noostak.server.group.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.noostak.server.group.common.GroupErrorCode;
import org.noostak.server.group.common.GroupException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupNameTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 그룹 이름으로 객체 생성")
        @CsvSource({
                "TeamAlpha",
                "한글그룹",
                "Group123",
                "ValidGroupName",
                "1234567890",
                "Mixed123한글",
                "특수문자#!!@!#",
                "띄어쓰기     "
        })
        void shouldCreateGroupNameSuccessfully(String validName) {
            GroupName groupName = GroupName.from(validName);
            assertThat(groupName.value()).isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("그룹 이름이 null이거나 비어 있는 경우")
        @NullAndEmptySource
        void shouldThrowExceptionForNullOrEmptyName(String invalidName) {
            assertThatThrownBy(() -> GroupName.from(invalidName))
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.GROUP_NAME_NOT_EMPTY.getMessage());
        }

        @ParameterizedTest
        @DisplayName("그룹 이름의 길이가 30자를 초과하는 경우")
        @CsvSource({
                "abcdefghijklmnopqrstuvwxyz12345",
                "1234567890123456789012345678901"
        })
        void shouldThrowExceptionForNameExceedingMaxLength(String invalidName) {
            assertThatThrownBy(() -> GroupName.from(invalidName))
                    .isInstanceOf(GroupException.class)
                    .hasMessageContaining(GroupErrorCode.INVALID_GROUP_NAME_LENGTH.getMessage());
        }
    }
}
